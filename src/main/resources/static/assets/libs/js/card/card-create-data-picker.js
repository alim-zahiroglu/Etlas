// initialize the dataRangePicker for perches-data and payed-data
$(function () {
    $('#car-due-date').daterangepicker({
        showDropdowns: true,
        singleDatePicker: true,
        timePicker: false,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        autoApply:true,
        locale: {
            format: 'DD/MM' // Adjust the format to display only 24-hour time
        },
        // startDate: moment()
    });
});