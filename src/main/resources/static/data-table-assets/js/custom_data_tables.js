$(document).ready(function () {
    // Filter table rows based on input value
    $("#my_table_search").on("keyup", function () {
        let value = $(this).val().toLowerCase();
        $("#my_data_table tr").toggle(function () {
            return $(this).text().toLowerCase().indexOf(value) > -1;
        });
    });

    // Initialize DataTable
    const myDataTable = $('#my_data_table').DataTable();

    // Initialize tooltips
    $('[data-toggle="tooltip"]').tooltip();

    // Initialize DataTable with buttons
    const table = myDataTable.buttons(['copy', 'csv', 'excel', 'pdf', 'print']);

    // Move DataTable buttons to a specific container
    table.container().appendTo('#export_buttons_wrapper .col-md-6:eq(0)');
});
