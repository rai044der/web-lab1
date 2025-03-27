// Функция для обработки кнопки "Выполнено"
async function markAsProcessed(externalId) {
    try {
        if (confirm("Подтвердите, что хотите отметить заявку как обработанную") === false) {
            return;
        }
        const response = await fetch("/api/v1/call_back/internal", {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ externalId })
        });

        if (response.ok) {
            await loadCalls();  // Перезагружаем заявки
        } else {
            alert("Ошибка при обновлении заявки.");
        }
    } catch (error) {
        console.error('Ошибка при обновлении заявки:', error);
    }
}

// Функция для отображения деталей заявки в модальном окне
function showCallDetails(externalId) {
    const calls = JSON.parse(localStorage.getItem('calls')) || [];

    console.log(calls);

    const call = calls.find(c => c.externalId === externalId);
    if (call) {
        document.getElementById('modal-name').textContent = call.name;
        document.getElementById('modal-phone').textContent = call.phone;
        document.getElementById('modal-description').textContent = call.description;
    }

    const modal = document.getElementById("calls-modal");
    modal.showModal();
}

// Функция для загрузки заявок
async function loadCalls() {

    const includeProcessedCheckbox = document.getElementById("includeProcessed");
    const callsTableBody = document.getElementById("calls-table-body");

    const includeProcessed = includeProcessedCheckbox.checked;
    const url = `/api/v1/call_back/internal?includeProcessed=${includeProcessed}`;

    try {
        const response = await fetch(url);
        const calls = await response.json();
        localStorage.setItem('calls', JSON.stringify(calls));

        // Очищаем таблицу перед обновлением
        callsTableBody.innerHTML = '';

        calls.forEach(call => {
            const tr = document.createElement('tr');

            tr.innerHTML = `
                    <td style="display:none;">${call.externalId}</td>
                    <td>${call.phone}</td>
                    <td>
                        ${call.processed ? '<span class="badge bg-success">Обработано</span>' : '<span class="badge bg-warning">Ожидает</span>'}
                    </td>
                    <td>${call.created}</td>
                    <td>
                        ${!call.processed ? `<button class="btn btn-success btn-sm" onclick="markAsProcessed('${call.externalId}')">Выполнено</button>` : ''}
                        <button class="btn btn-info btn-sm" data-bs-toggle="modal" data-bs-target="#callDetailModal" onclick="showCallDetails('${call.externalId}')">Подробнее</button>
                    </td>
                `;

            callsTableBody.appendChild(tr);
        });
    } catch (error) {
        console.error('Ошибка загрузки данных:', error);
    }
}

let autoRefreshEnabled = false;
let fetchInterval = null;
let blinkInterval = null;

document.addEventListener("DOMContentLoaded", function () {
    const includeProcessedCheckbox = document.getElementById("includeProcessed");
    const autoRefreshBtn = document.getElementById("auto-refresh-btn");

    // Обработчик изменения состояния тумблера includeProcessed
    includeProcessedCheckbox.addEventListener('change', loadCalls);

    // Обработчик включения автообновления
    autoRefreshBtn.addEventListener('click', function () {
        autoRefreshEnabled = !autoRefreshEnabled;

        if (autoRefreshEnabled) {
            // Зеленая кнопка при включении автообновления
            autoRefreshBtn.textContent = "Отключить автообновление";
            blinkInterval = setInterval(() => {
                autoRefreshBtn.classList.toggle("btn-info");
                autoRefreshBtn.classList.toggle("btn-secondary");
            }, 500); // Меняет цвет каждые 500 мс
            fetchInterval = setInterval(loadCalls, 3000);  // Обновление каждую секунду
        } else {
            // Возвращаем обычный стиль
            clearInterval(blinkInterval);
            autoRefreshBtn.classList.remove("btn-info");
            autoRefreshBtn.classList.add("btn-secondary");
            autoRefreshBtn.textContent = "Включить автообновление";
            clearInterval(fetchInterval);  // Останавливаем обновление
        }

        // Загружаем данные вручную при активации
        loadCalls();
    });

    // Загружаем данные при загрузке страницы
    loadCalls();
});
