/**
 * TODO:
 *
 ** -- getUsers
 * -- addNewUser
 * -- editUser
 * -- deleteUser
 */

window.onload = getUsersData();

function updateUserList(data) {
    console.log(data)
    detailList.innerHTML = '';
    detailList.innerHTML = `
    ${data.map(({userId, firstName, lastName, role, login, password}) => {
        return `
                             <div class="user__item test" data-user-id = ${userId}>
                                <div class="row user__text" style="flex-wrap: nowrap; width: 60%;">
                                    <a style="width: 290px;">${firstName + ' ' + lastName}</a>
                                    <a style="color: #F78F0F;">${login}</a>
                                    <div class="col-md-3 test__control text-md-end">
                                        <button class="user__edit-button" th:action="@{/admin/editUser}" ><img src="/img/edit-icon.svg" alt="Edit user" class="icon-btn"></button>
                                        <button class="user__delete-button" ><img src="/img/delete-icon.svg" data-bs-target="#deleteUserModal" data-bs-toggle="modal" alt="Delete user" class="icon-btn"></button>
                                    </div>
                                </div>
                             </div>
      `
    }).join('')}
  `
}

const token = document.head.querySelector('meta[name="_csrf"]').getAttribute('content');
const detailList = document.getElementById('detailList');
// const token = "${_csrf.token}";
let dataByUsers = null;

async function getUsersData() {
    const url = new URL("http://localhost:8080/admin/getUsers");
    const params = {};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url);
    let result = await response.json();
    dataByUsers = result;
    updateUserList(result);
}

const createNewUserForm = document.getElementById('newUserForm');
const newUserFormCloseButton = document.getElementById('newUserFormCloseButton');

async function addNewUser(firstName, surname, role, login, password) {
    newUserFormCloseButton.click();
    const url = new URL("http://localhost:8080/admin/addUser");
    let params = {firstName, surname, role, login, password};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": token
        }
    });
    const result = await response.json();
    console.log(result[0].userId);
    dataByUsers = result;
    updateUserList(result);
}

async function editUser(userId, firstName, surname, role, login, password) {
    newUserFormCloseButton.click();
    const url = new URL("http://localhost:8080/admin/editUser");
    let params = {userId, firstName, surname, role, login, password};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": token
        }
    });
    const result = await response.json();
    console.log(result[0].userId);
    dataByUsers = result;
    updateUserList(result);
}

async function deleteUser(target) {
    const userItem= target.closest('.user__item');
    const id = userItem.dataset.userId;
    const url = new URL("http://localhost:8080/admin/removeUser");
    let params = {userId: id};
    url.search = new URLSearchParams(params).toString();
    const response = await fetch(url, {
        method: 'DELETE',
        headers: {
            "Content-Type": "application/json",
            "X-CSRF-TOKEN": token
        }
    });
    const result = await response.json();
    dataByUsers = result;
    updateUserList(result);
}

createNewUserForm.addEventListener('submit', (event) => {
    event.preventDefault();
    const formData = new FormData(createNewUserForm);
    const firstName = formData.get('userFirstName');
    const surname = formData.get('userLastName');
    const role = formData.get('userRole');
    const login = formData.get('userLogin');
    const password = formData.get('userPassword');
    if (isNewUser) {
        addNewUser(firstName, surname, role, login, password);
    } else {
        editUser(currentUserId, firstName, surname, role, login, password);
    }
    createNewUserForm.reset();
});

function setCreateUserFormStartData() {
    if (dataByUsers != null && dataByUsers.length !== 0) {
        const {firstName, lastName, role, login, password} = dataByUsers.find(({userId}) => {
            return userId == currentUserId
        });
        createNewUserForm.querySelector('[name=userFirstName]').value = firstName;
        createNewUserForm.querySelector('[name=userLastName]').value = lastName;
        createNewUserForm.querySelector('[name=userRole]').value = role.currentRole;
        createNewUserForm.querySelector('[name=userLogin]').value = login;
        createNewUserForm.querySelector('[name=userPassword]').value = password;
    }
}

function createUserClickHandler(event) {
    const {target, isTrusted} = event;
    console.log(event)
    const openFormButton = target.closest('#createNewUserButton');
    if (openFormButton) {
        createNewUserForm.reset();
    }
    if (!isTrusted) {
        setCreateUserFormStartData();
        isNewUser = false;
    } else {
        isNewUser = true;
    }
}

document.addEventListener('click', (event) => {
    const {target} = event;
     if (target.closest('.detail__create')) {
        createUserClickHandler(event);
    } else if (target.closest('#detailList')) {
}
})

let currentUserId = null;
let isNewUser = true;
const createNewUserButton = document.getElementById('createNewUserButton');

function setCurrentUserId(target) {
    const id = target.closest('.user__item').dataset.userId;
    currentUserId = id;
}

function clickUserHandler(target) {
    setCurrentUserId(target);
    if (target.classList.contains('user__text')) {
        target.closest('.test').classList.toggle('open');
    } else if (target.closest('.user__edit-button')) {
        isNewUser = false;
        createNewUserButton.click();
    } else if (target.closest('.user__delete-button')) {
        deleteTarget = target;
    }
}

let deleteTarget = null;
function deleteUserClick(){
    deleteUser(deleteTarget);
    deleteTarget = null;
}

detailList.addEventListener('click', ({target}) => {
    if (target.closest('.test')) {
        clickUserHandler(target);
    }
})
