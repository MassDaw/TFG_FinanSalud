package com.proyecto.tfg_finansalud.services;

import com.mongodb.client.result.UpdateResult;
import com.proyecto.tfg_finansalud.entities.*;
import com.proyecto.tfg_finansalud.repositories.UserRepository;
import com.proyecto.tfg_finansalud.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Usuario save(Usuario user) {
        Optional<Usuario> usuario = userRepository.findByUsername(user.getUsername());
        if (usuario.isPresent()) {
            System.out.println("El usuario ya existe");
        }
        
        // Establecer el estado de verificación como falso
        user.setEmailVerified(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Guardar el usuario
        userRepository.save(user);
        
        // Crear token de verificación
        String token = generateVerificationToken();
        VerificationToken verificationToken = new VerificationToken(token, user.getId());
        tokenRepository.save(verificationToken);
        
        // Enviar email de verificación
        try{
            emailService.sendVerificationEmail(user.getEmail(), token);
        }catch (Exception e){
            System.out.println("Error al enviar el email de verificación: " + e.getMessage());
        }


        return user; // Retorna el usuario guardado
    }

    public boolean verifyEmail(String token) {
        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
        
        if (verificationToken.isPresent() && !verificationToken.get().isExpired() && !verificationToken.get().isUsed()) {
            VerificationToken vToken = verificationToken.get();
            Optional<Usuario> user = userRepository.findById(vToken.getUserId());
            
            if (user.isPresent()) {
                Usuario usuario = user.get();
                usuario.setEmailVerified(true);
                userRepository.save(usuario);
                
                // Marcar el token como usado
                vToken.setUsed(true);
                tokenRepository.save(vToken);
                return true;
            }
        }
        return false;
    }

    private String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
    
    // ... resto de los métodos existentes ...
    //Obtiene todos los presupuestos del usuario del MES EN CURSO
    public List<Budget> getBudget() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets().stream()
                .filter(a->a.getYearMonth().equals(YearMonth.now().atDay(1))).collect(Collectors.toList());
    }
    //Obtiene todos los presupuestos del usuario del MES EN CURSO Y solo retorna los id
    public List<String> getBudgetID() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets().stream()
                .filter(a->a.getYearMonth().equals(YearMonth.now().atDay(1))).map(Budget::getId).collect(Collectors.toList());
    }
    public List<String> getIncomeID() {
        return userRepository.findByUsername(getAuthenticatedUsername())
                .map(user -> Optional.ofNullable(user.getIncome())
                        .orElse(Collections.emptyList())
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(income -> Optional.ofNullable(income.getDate())
                                .map(date -> date.equals(YearMonth.now().atDay(1)))
                                .orElse(false))
                        .map(Income::getId)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public List<String> getAllBudgetID() {
        return userRepository.findByUsername(getAuthenticatedUsername()).get().getBudgets().stream().map(Budget::getId).collect(Collectors.toList());
    }

    //ASIGNA UN NUEVO BUDGET al usuario
    public void userNewBudget(Budget budget) throws Exception {
        String username = getAuthenticatedUsername();
        Query query = new Query(Criteria.where("username").is(username));

        Update update = new Update().push("budgets", budget);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Usuario.class);

        if (result.getModifiedCount() == 0) {
            throw new Exception("Usuario no encontrado o no modificado");
        }
    }
    //BASADO EN EL NOMBRE DEL BUDGET, devuelve el ID del budget que sea del mes actual
    //Recibe un boolean que opcionalmente puede borrar el budget o no
    public String returnBudgetIDfromUser(String budgetName, boolean remove) throws Exception {
        try {
            Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));
            if (user.isPresent()) {
                //el budget borrado debe coincidir con el NOMBRE Y FECHA ACTUAL PARA QUE NO SE ELIMINE REGISTROS ANTERIORES

                Optional<Budget> budget = user.get().getBudgets().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(budgetName) && a.getYearMonth().equals(YearMonth.now().atDay(1)))
                        .findFirst();
                if (budget.isEmpty()) {
                    return "nope";
                }
                if (remove)user.get().getBudgets().remove(budget.get());
                userRepository.save(user.get());
                return budget.get().getId();
            }
        }catch (Exception e){
            throw new Exception("Usuario no encontrado");
        }
        throw new Exception("Usuario no encontrado");
    }



    //obtener nombre de usuario autenticado
    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString(); // Si el principal es solo un string
        }
    }
    public Income getIncomeByBudgetName(String budgetName) throws Exception {
        Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));
        if (user.isEmpty()) {
            throw new Exception("Usuario no encontrado");
        }
        System.out.println(user.get().getIncome());
        List<String> incomeIds = Optional.ofNullable(user.get().getIncome())
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)  // Filtra elementos nulos
                .map(Income::getId)
                .collect(Collectors.toList());



        LocalDate now = LocalDate.now();
        Query query = new Query();
        query.addCriteria(
                Criteria.where("id").in(incomeIds)
                        .and("date").gte(now.withDayOfMonth(1)) // desde el primer día del mes
                        .lte(now.withDayOfMonth(now.lengthOfMonth()))
                        .and("name").is(budgetName)// hasta el último día del mes
        );

        return mongoTemplate.findOne(query, Income.class);
    }

    public Double getIncome (){
        Optional<Usuario> user = userRepository.findByUsername((getAuthenticatedUsername()));

        List<Income> income = user.get().getIncome().stream().filter(a -> a.getDate().getMonth().equals(LocalDate.now().getMonth())).toList();



        return income.stream().mapToDouble(Income::getIncomes).sum();
    }

    public void userNewIncome(Income income) throws Exception {
        String username = getAuthenticatedUsername();
        Query query = new Query(Criteria.where("username").is(username));
        Update update = new Update().push("income", income);
        UpdateResult result = mongoTemplate.updateFirst(query, update, Usuario.class);

        if (result.getModifiedCount() == 0) {
            throw new Exception("Usuario no encontrado o no modificado");
        }
    }
    public String saveProfileImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get("uploads/profile-pics", fileName);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return "/uploads/profile-pics/" + fileName; // URL para acceder a la imagen
    }
    public void updateProfileImage(String username, String imageUrl) {
        Usuario user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }
    public void updateEmail(String username, String newEmail) {
        Usuario user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setEmail(newEmail);
        userRepository.save(user);
    }
}