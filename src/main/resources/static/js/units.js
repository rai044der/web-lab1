// Функция для отображения карточек
function displayUnits(units) {
    const unitsContainer = document.getElementById("units");

    units.forEach(unit => {
        // Создаём карточку для каждого элемента
        const unitCard = document.createElement("div");
        unitCard.classList.add("card", "bg-base-100", "shadow-lg", "rounded-lg", "overflow-hidden", "flex", "flex-col", "items-center");

        // Фото
        const unitImage = document.createElement("img");
        unitImage.src = unit.photoLink;
        unitImage.alt = unit.name;
        unitImage.loading = "lazy";
        unitImage.classList.add("w-full", "h-48", "object-cover");
        unitCard.appendChild(unitImage);

        // Название
        const unitName = document.createElement("h2");
        unitName.classList.add("text-xl", "font-semibold", "mt-4");
        unitName.textContent = unit.name;
        unitCard.appendChild(unitName);

        // Кнопка Описание
        const unitDescriptionButton = document.createElement("button");
        unitDescriptionButton.textContent = "Описание";
        unitDescriptionButton.classList.add("btn", "mt-2", "w-full", "rounded-lg", "shadow-md", "py-2", "px-4", "transition", "transform", "hover:scale-105", "focus:outline-none");
        unitDescriptionButton.addEventListener("click", () => showDescription(unit));
        unitCard.appendChild(unitDescriptionButton);

        // Добавляем карточку на страницу
        unitsContainer.appendChild(unitCard);
    });
}

// Функция для отображения модального окна
function showDescription(unit) {
    const modal = document.getElementById("unit-modal");
    const modalTitle = document.getElementById("modal-title");
    const modalDescription = document.getElementById("modal-description");

    modalTitle.textContent = unit.name;
    modalDescription.textContent = unit.description;

    modal.showModal();
}

document.addEventListener("DOMContentLoaded", function () {

    // Загружаем данные с API
    fetch("/api/v1/unit")
        .then(response => response.json())
        .then(data => {
            displayUnits(data);
        })
        .catch(error => {
            console.error("Ошибка при загрузке данных:", error);
        });
});