const menuTableBody = document.getElementById("menu-table-body");
const addUnitBtn = document.getElementById("addUnitBtn");
const saveUnitBtn = document.getElementById("saveUnitBtn");

const unitIdInput = document.getElementById("unitId");
const unitNameInput = document.getElementById("unitName");
const unitDescriptionInput = document.getElementById("unitDescription");
const unitPhotoLinkInput = document.getElementById("unitPhotoLink");

let isEditing = false;

// Функция загрузки меню
async function loadMenu() {
    try {
        const response = await fetch("/api/v1/unit");
        const menuItems = await response.json();
        menuTableBody.innerHTML = '';

        menuItems.forEach(unit => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                    <td>${unit.name}</td>
                    <td>
                        <button class="btn btn-warning" onclick="deleteUnit('${unit.externalId}')">Удалить</button>
                        <button class="btn btn-info" onclick="editUnit('${unit.externalId}', '${unit.name}', '${unit.description}', '${unit.photoLink}')">Редактировать</button>
                    </td>
                `;
            menuTableBody.appendChild(tr);
        });
    } catch (error) {
        console.error("Ошибка при загрузке меню:", error);
    }
}

async function deleteUnit(externalId) {
    if (confirm("Вы уверены, что хотите удалить?")) {
        try {
            const response = await fetch(`/api/v1/unit/${externalId}/internal`, { method: "DELETE" });
            if (response.ok) {
                alert("Успешно удалено!");
                loadMenu();
            } else {
                alert("Ошибка при удалении.");
            }
        } catch (error) {
            console.error("Ошибка при удалении:", error);
        }
    }
}

document.addEventListener("DOMContentLoaded", function () {

    // Функция открытия модального окна для редактирования
    window.editUnit = function (externalId, name, description, photoLink) {
        isEditing = true;
        unitIdInput.value = externalId;
        unitNameInput.value = name;
        unitDescriptionInput.value = description;
        unitPhotoLinkInput.value = photoLink;
        document.getElementById("editUnitModalLabel").textContent = "Редактировать";
        document.getElementById("unit-modal").showModal();
    };

    // Функция открытия модального окна для добавления
    addUnitBtn.addEventListener("click", function () {
        isEditing = false;
        unitIdInput.value = "";
        unitNameInput.value = "";
        unitDescriptionInput.value = "";
        unitPhotoLinkInput.value = "";
        document.getElementById("editUnitModalLabel").textContent = "Добавить";
        document.getElementById("unit-modal").showModal();
    });

    // Функция сохранения изменений (добавление/редактирование)
    saveUnitBtn.addEventListener("click", async function () {
        const unitData = {
            name: unitNameInput.value,
            description: unitDescriptionInput.value,
            photoLink: unitPhotoLinkInput.value
        };

        try {
            let response;
            if (isEditing) {
                unitData.externalId = unitIdInput.value;
                response = await fetch("/api/v1/unit/internal", {
                    method: "PATCH",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(unitData)
                });
            } else {
                response = await fetch("/api/v1/unit/internal", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(unitData)
                });
            }

            if (response.ok) {
                alert(isEditing ? "Успешно обновлено!" : "Успешно добавлено!");
                document.getElementById("closeModalBtn").click();
                await loadMenu();
            } else {
                alert("Ошибка при сохранении.");
            }
        } catch (error) {
            console.error("Ошибка при сохранении:", error);
        }
    });

    // Загрузка меню при старте
    loadMenu();
});
