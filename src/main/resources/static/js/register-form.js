document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("loginForm");
    if (!registerForm) return;

    registerForm.addEventListener("submit", (event) => {
        event.preventDefault();

        const username = document.getElementById("username").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;

        const usernameError = document.getElementById("username-error");
        const emailError = document.getElementById("email-error");
        const passwordError = document.getElementById("password-error");

        let isValid = true;

        if (username === "") {
            usernameError.textContent = "Por favor, introduce tu nombre de usuario";
            isValid = false;
        } else {
            usernameError.textContent = "";
        }

        if (!email.includes("@")) {
            emailError.textContent = "Por favor, introduce un correo electrónico válido";
            isValid = false;
        } else {
            emailError.textContent = "";
        }

        if (password.length < 6) {
            passwordError.textContent = "La contraseña debe tener al menos 6 caracteres";
            isValid = false;
        } else {
            passwordError.textContent = "";
        }

        if (!isValid) return;

        const formData = { username, email, password };

        fetch("/api/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(formData),
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert("Registro exitoso. Ahora puedes iniciar sesión.");
                    window.location.href = "../user_login/user_login.html";
                } else {
                    alert(data.message || "Ocurrió un error al registrarse.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("Ocurrió un error al registrar la cuenta. Intenta nuevamente.");
            });
    });
});
