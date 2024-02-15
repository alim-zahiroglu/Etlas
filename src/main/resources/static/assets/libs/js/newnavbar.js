const sidebarToggle = document.querySelector("#sidebar-toggle");
sidebarToggle.addEventListener("click",function(){
    document.querySelector("#sidebar").classList.toggle("collapsed");
    document.querySelector("#sidebar1").classList.toggle("collapsed");
    document.querySelector("#sidebar2").classList.toggle("collapsed");
});
const sidebarToggle1 = document.querySelector("#sidebar-toggle1");
sidebarToggle1.addEventListener("click",function(){
    document.querySelector("#sidebar").classList.toggle("collapsed");
    document.querySelector("#sidebar1").classList.toggle("collapsed");
    document.querySelector("#sidebar2").classList.toggle("collapsed");
});

var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
    return new bootstrap.Tooltip(tooltipTriggerEl);
});