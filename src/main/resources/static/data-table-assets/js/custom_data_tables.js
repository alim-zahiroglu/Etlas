//
// $(document).ready(function () {
//     // Initialize DataTable with Buttons and disable searching
//     const table = $('#example').DataTable({
//         buttons: ['copy','print', 'pdf', 'excel', 'csv'],
//         lengthMenu: [[10, 25, 50, 100, -1], [10, 25, 50, 100, 'Show All']],
//         dom: '<"row"<"col-4"l><"col-4 text-center"B><"col-4"f>>rt<"row"<"col-4"l><"col-4 text-center"i><"col-4 text-right"p>>',
//     });
//
//     // Manually handle Copy button
//     $('#more_actions_copy').on('click', function () {
//         table.button(0).trigger();
//     });
//
//     // Manually handle Print button
//     $('#more_actions_print').on('click', function () {
//         table.button(1).trigger();
//     });
//
//     // Manually handle pdf export
//     $('#export_pdf').on('click', function () {
//         table.button(2).trigger();
//     });
//
//     // Manually handle excel export
//     $('#export_excel').on('click', function () {
//         table.button(3).trigger();
//     });
//
//     // Manually handle cvs export
//     $('#export_csv').on('click', function () {
//         table.button(4).trigger();
//     });
//
//     // $('#example td').css('white-space', 'nowrap');
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


$(document).ready(function () {
    // Initialize DataTable with Buttons and disable searching
    const table = $('#example').DataTable({
        buttons: ['copy','print', 'pdf', 'excel', 'csv'],
        lengthMenu: [[10, 25, 50, 100, -1], [10, 25, 50, 100, 'Show All']],
        dom: '<"row"<"col-4"l><"col-4 text-center"B><"col-4"f>>rt<"row"<"col-4"l><"col-4 text-center"i><"col-4 text-right"p>>',
        searching: true,
        search: {
            smart: false,
        },
        headerCallback: function(thead, data, start, end, display) {
            // Add a class to the header row
            $(thead).addClass('exclude-from-search');
        },
    });

    // Manually handle Copy button
    $('#more_actions_copy').on('click', function () {
        table.button(0).trigger();
    });

    // Manually handle Print button
    $('#more_actions_print').on('click', function () {
        table.button(1).trigger();
    });

    // Manually handle pdf export
    $('#export_pdf').on('click', function () {
        table.button(2).trigger();
    });

    // Manually handle excel export
    $('#export_excel').on('click', function () {
        table.button(3).trigger();
    });

    // Manually handle cvs export
    $('#export_csv').on('click', function () {
        table.button(4).trigger();
    });

    // $('#example td').css('white-space', 'nowrap');

    // Enable tooltip for elements with data-toggle="tooltip"
    $('[data-toggle="tooltip"]').tooltip();

    // Filter table rows based on input value
    $("#myInput").on("keyup", function () {
        let value = $(this).val().toLowerCase();
        // Use :not selector to exclude rows with the specified class
        $("#example tr:not(.exclude-from-search)").filter(function () {
            $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
        });
    });
});
