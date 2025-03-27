async function loadLayout() {
    document.getElementById("header").innerHTML = await fetch("fragments/header.html").then(res => res.text());
    document.getElementById("footer").innerHTML = await fetch("fragments/footer.html").then(res => res.text());
    await checkAuthStatus();
}

async function checkAuthStatus() {
    try {
        const response = await fetch("/api/v1/auth/check");
        const isAuthenticated = await response.json();

        let navMenu = document.getElementById("nav-menu");
        let navMenuShadow = document.getElementById("nav-menu-shadow");
        let authButton = document.getElementById("auth-button");

        if (isAuthenticated) {
            // Добавляем пункт "Звонки"
            let callsLink = document.createElement("a");
            callsLink.className = "btn btn-ghost";
            callsLink.href = "/admin_calls.html";
            callsLink.innerText = "Журнал звонков";
            let callsLinkLi = document.createElement("li");
            callsLinkLi.appendChild(callsLink);
            navMenu.appendChild(callsLinkLi);

            callsLink = document.createElement("a");
            callsLink.className = "btn btn-ghost";
            callsLink.href = "/admin_calls.html";
            callsLink.innerText = "Журнал звонков";
            callsLinkLi = document.createElement("li");
            callsLinkLi.appendChild(callsLink);
            navMenuShadow.appendChild(callsLinkLi);

            // Добавляем пункт "Редактор"
            let editorLink = document.createElement("a");
            editorLink.className = "btn btn-ghost";
            editorLink.href = "/admin_editor.html";
            editorLink.innerText = "Редактор";
            let editorLinkLi = document.createElement("li");
            editorLinkLi.appendChild(editorLink);
            navMenu.appendChild(editorLinkLi);

            editorLink = document.createElement("a");
            editorLink.className = "btn btn-ghost";
            editorLink.href = "/admin_editor.html";
            editorLink.innerText = "Редактор";
            editorLinkLi = document.createElement("li");
            editorLinkLi.appendChild(editorLink);
            navMenuShadow.appendChild(editorLinkLi);

            // Меняем кнопку на "Выйти"
            authButton.innerHTML = '<a class="btn btn-warning" href="/logout">Выйти</a>';
        }
    } catch (error) {
        console.error("Ошибка при проверке авторизации:", error);
    }
}

document.addEventListener("DOMContentLoaded", loadLayout);
