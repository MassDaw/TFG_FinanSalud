document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    const usernameInput = document.getElementById("username");
    const passwordInput = document.getElementById("password");
    const usernameError = document.getElementById("username-error");
    const passwordError = document.getElementById("password-error");
    const correoInput = document.getElementById("correo");
    const correoError = document.getElementById("correo-error");

    // Validación del formulario
    loginForm.addEventListener("submit", (event) => {
        let isValid = true;

        // Validar usuario
        if (usernameInput.value.trim() === "") {
            usernameError.textContent = "Por favor, introduce tu nombre de usuario";
            isValid = false;
        } else {
            usernameError.textContent = "";
        }

        // Validar contraseña
        if (passwordInput.value === "") {
            passwordError.textContent = "Por favor, introduce tu contraseña";
            isValid = false;
        } else if (passwordInput.value.length < 6) {
            passwordError.textContent =
                "La contraseña debe tener al menos 6 caracteres";
            isValid = false;
        } else {
            passwordError.textContent = "";
        }

        // Validar correo electrónico (solo en formulario de registro)
        if (correoInput.value === "") {
            correoError.textContent =
                "Por favor, introduce un correo electrónico válido";
            isValid = false;
        } else {
            correoError.textContent = "";
        }

        // Si hay errores, prevenir el envío del formulario
        if (!isValid) {
            event.preventDefault();
            return;
        }

        // Si todo está correcto, enviar los datos al servidor
        event.preventDefault(); // Prevenir el envío normal para usar AJAX

        // Crear objeto con los datos del formulario
        const formData = {
            username: usernameInput.value,
            password: passwordInput.value,
        };

        // Enviar datos al servidor usando fetch API
        fetch("/api/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(formData),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Error en la respuesta del servidor");
                }
                return response.json();
            })
            .then((data) => {
                if (data.success) {
                    // Redireccionar a la página principal si el login es exitoso
                    window.location.href = "/dashboard";
                } else {
                    // Mostrar mensaje de error
                    alert(
                        data.message ||
                        "Credenciales incorrectas. Por favor, inténtalo de nuevo."
                    );
                }
            })
            .catch((error) => {
                console.error("Error:", error);
                alert(
                    "Ha ocurrido un error al intentar iniciar sesión. Por favor, inténtalo de nuevo más tarde."
                );
            });
    });
});
