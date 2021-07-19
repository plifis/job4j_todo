function addItem() {
    if (!validateItem()) {
        return false;
    }

    let options = Array.from(document.getElementById('categories').selectedOptions).map(el=>el.value);
    let selectArr = [];
    let y = 0;
    for (let i = 0; i < options.length; i++) {
            selectArr[y] = options[i];
            y++;
        }
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/item/item.do',
        data: {"description": $("#description").val(), "categories": options},
        data_type: 'text'
    }).done(function (resp) {
        alert(resp);
    }).fail(function (err) {
        alert("Что то пошло не так" + err);
    });
}

function getAllItems () {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/item/item.do',
        data_type: 'text'
    }).done( function (resp){
        loadListCategories();
        let arrJson = JSON.parse(resp);
        for (let i = 0; i < arrJson.length; i++) {
            let desc = arrJson[i].description;
            let id = arrJson[i].id;
            let created = arrJson[i].created;
            let done = '';
            let disabled = '';
            let user;
            let userObject = arrJson[i].user;
            let category = "";
            arrJson[i].categories.forEach(function (categories) {
               category += categories.name + ", ";
            });
            if (userObject !== undefined) {
                user = userObject.name;
            }
            if (arrJson[i].done === true) {
                done = 'checked';
            }
            if (done === 'checked') {
                disabled = 'disabled';
            }
            $('tbody:last').append(
                '<tr><td><label class="form-check-label" for="item' + id + '">' + id + '</label></td>' +
                '<td><label class="form-check-label" for="item' + id + '">' + desc + '</label></td>' +
                '<td><label class="form-check-label" for="item"' + id +  '"</td>' + created + '</label></td>' +
                '<td><input class="form-check-input" type="checkbox"' + done + ' ' + disabled + ' ' +
                'name="item' + id +  '" onclick="replaceItem(' + id +')"></td> +' +
                '<td><label class="form-check-label" for="item' + id + '">' + user + '</label></td>' +
                '<td><label class="form-check-label" for="categories">' + category +'</label> </td></tr>');
        }
    }).fail( function (){
        alert("не смог получить все записи");
    });
}

function watchAllItems() {
        if (document.getElementById('watchAll').checked === false) {
            $('input').each(function () {
                let id = $(this).attr('id');
                if (($(this).attr('checked')) && (id !== 'watchAll')) {
                    $(this).parent().parent().hide();
                }
            });
        } else {
            $('input').each(function () {
                if (($(this).attr('checked'))) {
                    $(this).parent().parent().show();
                }
            });
        }
}


function replaceItem(id) {
    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/item/replace.do',
        data: 'id=' + id,
        data_type: 'text'
    }).done(function (){
        location.reload();
    }).fail(function (){
        alert('Запись не была обновлена');
    })
}

function checkUser () {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/item/auth.do',
        data: {"login": $("#login").val(), "password": $('#password').val()},
        data_type: 'text'
    }).done(function (resp) {
    }).fail(function () {
    })
}

function validateRegistration() {
    let name = $('#name').val();
    let email = $('#email').val();
    let password = $('#password').val();
    if ((name === "") || (email === "") || (password === "")) {
        alert("Все поля должны быть заполнены");
        return false;
    } else {
        return true;
    }
}

function validateLogin() {
        let login = $('#login').val();
        let password = $('#password').val();
        if ((login === "") || (password === "")) {
            alert("Все поля должны быть заполнены");
            return false;
        } else {
            return true;
        }
}

function validateItem() {
    let desc = $('#description').val();
    if (desc === "") {
        alert("Необходимо заполнить поле Description");
        return false;
    } else {
        return  true;
    }
}

function loadListCategories() {
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/item/categories.do',
        data_type: 'text'
    }).done( function (resp){
        let arrCategories =JSON.parse(resp);
        for (let i = 0; i < arrCategories.length; i++) {
            let id = arrCategories[i].id;
            let name = arrCategories[i].name;
            $('select').append(
                '<option name="categories" id="categories" value=' + id + '>' + name +'</option>')
        }
    }).fail( function (err){
        alert("Что то пошло не так" + err);
    })
}
