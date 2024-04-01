
// setup select2 dropdowns
$(document).ready(function () {
    function setupSelect2($select, $selectorArrow) {
        $select.select2({
            allowClear: false,
            closeOnSelect: true,
        });

        $select.on('select2:open', () => {
            $selectorArrow.removeClass('country-rotate-down').addClass('country-rotate-up');
        });

        $select.on('select2:close', () => {
            $selectorArrow.removeClass('country-rotate-up').addClass('country-rotate-down');
        });
    }


    const $selectedGiver = $('#mySelectForGiver');
    const $selectedGiverArrow = $('#giver-selector-arrow');
    setupSelect2($selectedGiver, $selectedGiverArrow);

    const $selectedReceiver = $('#mySelectForReceiver');
    const $selectedReceiverArrow = $('#receiver-selector-arrow');
    setupSelect2($selectedReceiver, $selectedReceiverArrow);

    const $selectedCArd = $('#mySelectForCard');
    const $selectedCardArrow = $('#receiver-card-selector-arrow');
    setupSelect2($selectedCArd, $selectedCardArrow);

});

// initialize the dataRangePicker for perches-data and payed-data
$(function () {
    $('.date-picker').daterangepicker({
        showDropdowns: true,
        singleDatePicker: true,
        timePicker: false,
        timePicker24Hour: true,
        timePickerIncrement: 5,
        autoApply:true,
        locale: {
            format: 'DD-MM-YYYY' // Adjust the format to display only 24-hour time
        },
        // startDate: moment()
    });
});

// switch the currency units
$(document).ready(function () {
    // Handle currency dropdown change for perchesPrice
    $('#transactionAmount .change-currency').on('click', function () {
        const currencySymbol = $(this).data('currency-symbol');
        const selectedCurrency = $(this).data('selected-currency');
        $('#transactionAmount .currency-symbol').text(currencySymbol);
        $('#selectedCurrencyUnit').val(selectedCurrency);
    });
});


// switch the paid type
document.addEventListener('DOMContentLoaded', function () {
    const paidTypeSelect = document.getElementById('selected-paid-type');
    const receivedUserSelect = document.getElementById('received-user');
    const receivedCardSelect = document.getElementById('receiver-card');
});

// switch the paid type
$(document).ready(function () {
    // Handle byHand checkbox click
    $('#type-byHand').on('change', function () {
        if ($(this).prop('checked')) {
            // If byHand is checked, uncheck the byCard
            $('#type-byCard').prop('checked', false);
            showIndividualSections();
        } else {
            $('#type-byCard').prop('checked', true);
            showCompanySections();
        }
    });

    // Handle card checkbox click
    $('#type-byCard').on('change', function () {
        if ($(this).prop('checked')) {
            // If byCard is checked, uncheck the byHand
            $('#type-byHand').prop('checked', false);
            showCompanySections();
        } else {
            $('#type-byHand').prop('checked', true);
            showIndividualSections();
        }
    });

    // Initial state
    if ($('#type-byHand').prop('checked')) {
        showIndividualSections();
    } else if ($('#type-byCard').prop('checked')) {
        showCompanySections();
    }
});

function showIndividualSections() {
    $('#companySection').hide();
    $('#officePhoneSection').hide();
    $('#individualSection').show();
    $('#lastNameSection').show();
    $('#genderSelection').show();
}

function showCompanySections() {
    $('#companySection').show();
    $('#officePhoneSection').show();
    $('#individualSection').hide();
    $('#lastNameSection').hide();
    $('#genderSelection').hide();
}



