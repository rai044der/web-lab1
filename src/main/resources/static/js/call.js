document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("callback-form");
    const submitButton = document.getElementById("submit-btn");

    // Функция для проверки валидности формы
    function validateForm() {
        const name = document.getElementById("name").value.trim();
        const phone = document.getElementById("phone").value.trim();

        // Проверка на валидность
        submitButton.disabled = !(name && phone);

        // Включаем/выключаем форму на основе валидности
        form.classList.toggle('was-validated', name && phone);
    }

    // Слушаем изменения в полях формы
    form.addEventListener("input", validateForm);

    // Обработчик отправки формы
    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = {
            name: document.getElementById("name").value.trim(),
            phone: document.getElementById("phone").value.trim(),
            description: document.getElementById("description").value.trim()
        };

        try {
            const response = await fetch("/api/v1/call_back", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                alert("Спасибо! Мы перезвоним вам в ближайшее время!");
                form.reset();
                submitButton.disabled = true;
                window.location.href = '/';
            } else {
                alert("Произошла ошибка. Попробуйте снова.");
            }
        } catch (error) {
            console.error("Ошибка при отправке данных:", error);
            alert("Произошла ошибка. Попробуйте снова.");
        }
    });

    // Изначально проверяем форму
    validateForm();
});
