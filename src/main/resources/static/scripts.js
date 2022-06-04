const general = './exchange/';

function loadSelect() {
    $.ajax({
        url: general + 'currencies',
        method: 'GET',
        complete: function (data) {

            let codesList = JSON.parse(data.responseText);
            let selects = document.querySelector("#codes_select");
            selects.innerHTML = '';

            for (let i = 0; i < codesList.length; i++) {
                let option = document.createElement("option");
                option.value = codesList[i];
                option.text = codesList[i];
                selects.insertAdjacentElement("beforeend", option);
            }
        }
    })
}
