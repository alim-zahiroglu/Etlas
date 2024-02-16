
$(document).ready(function () {
    // Initialize DataTable with Buttons
    const table = $('#my-data-table').DataTable({
        buttons: ['copy', 'csv', 'excel', 'pdf', 'print']
    });

    // Move DataTable buttons container to a specific location
    table.buttons().container().appendTo('.col-md-6:eq(0)');

    // Enable tooltip for elements with data-toggle="tooltip"
    $('[data-toggle="tooltip"]').tooltip();

    // Filter table rows based on input value
    $("#myInput").on("keyup", function () {
        let value = $(this).val().toLowerCase();
        $("#my-data-table tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
});
