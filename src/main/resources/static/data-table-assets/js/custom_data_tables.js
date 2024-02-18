// $(document).ready(function () {
//     // Initialize DataTable with Buttons and disable searching
//     const table = $('#example').DataTable({
//         buttons: ['copy', 'csv', 'excel', 'pdf', 'print'],
//         lengthMenu: [10, 25, 50, 75, 100],
//     });
//
//     // Move DataTable buttons container to a specific location
//     table.buttons().container().appendTo('#example_wrapper .col-md-6:eq(0)').addClass('d-none');
//
//     // Manually handle Copy and Print buttons
//     $('#copy').on('click', function () {
//         table.button(0).trigger();
//     });
//
//     $('#print').on('click', function () {
//         table.button(4).trigger();
//     });
//
//     // Manually handle CSV, Excel, and PDF buttons
//     $('#export_pdf').on('click', function () {
//         table.button(3).trigger();
//     });
//
//     $('#export_Excel').on('click', function () {
//         table.button(2).trigger();
//     });
//
//     $('#export_CVS').on('click', function () {
//         table.button(1).trigger();
//     });
//
//     // Enable tooltip for elements with data-toggle="tooltip"
//     $('[data-toggle="tooltip"]').tooltip();
//
//     // Filter table rows based on input value
//     $("#myInput").on("keyup", function () {
//         let value = $(this).val().toLowerCase();
//         $("#example tr").filter(function () {
//             $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
//         });
//     });
// });
//
//


$(document).ready(function () {
    // Initialize DataTable with Buttons and disable searching
    const table = $('#example').DataTable({
        buttons: ['copy', 'csv', 'excel', 'pdf', 'print'],
        dom: '<"row"<"col-4"l><"col-4 text-center"B><"col-4"f>>rt<"row"<"col-4"l><"col-4 text-center"i><"col-4 text-right"p>>',
    });

    // Move DataTable buttons container to a specific location
    table.buttons().container().appendTo('#example_wrapper .col-md-6:eq(0)').addClass('d-none');

    // Manually handle Copy and Print buttons
    $('#copy').on('click', function () {
        table.button(0).trigger();
    });

    $('#print').on('click', function () {
        table.button(4).trigger();
    });

    // Manually handle CSV, Excel, and PDF buttons
    $('#export_pdf').on('click', function () {
        table.button(3).trigger();
    });

    $('#export_Excel').on('click', function () {
        table.button(2).trigger();
    });

    $('#export_CVS').on('click', function () {
        table.button(1).trigger();
    });

    // Enable tooltip for elements with data-toggle="tooltip"
    $('[data-toggle="tooltip"]').tooltip();

    // Filter table rows based on input value
    $("#myInput").on("keyup", function () {
        let value = $(this).val().toLowerCase();
        $("#example tr").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
});
