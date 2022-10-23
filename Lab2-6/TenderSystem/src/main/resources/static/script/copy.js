document.addEventListener("DOMContentLoaded", () => {
    const sharedLink = document.querySelector(".shared-link");

    sharedLink.addEventListener("click", e => {
        copyToClipboard(e.currentTarget.innerText);
    });
})

function copyToClipboard(text){
    navigator.clipboard.writeText(text)
        .catch(reason => console.log(reason));
}