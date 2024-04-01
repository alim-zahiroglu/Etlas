
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
$(document).ready(function () {
    // Handle byHand checkbox click
    $('#type-byHand').on('change', function () {
        if ($(this).prop('checked')) {
            // If byHand is checked, uncheck the byCard
            $('#type-byCard').prop('checked', false);
            removeCard();
        } else {
            $('#type-byCard').prop('checked', true);
            addCard();
        }
    });

    // Handle card checkbox click
    $('#type-byCard').on('change', function () {
        if ($(this).prop('checked')) {
            // If byCard is checked, uncheck the byHand
            $('#type-byHand').prop('checked', false);
            addCard();
        } else {
            $('#type-byHand').prop('checked', true);
            removeCard();
        }
    });

    // Initial state
    if ($('#type-byHand').prop('checked')) {
       removeCard();
    } else if ($('#type-byCard').prop('checked')) {
        addCard();
    }
});

function addCard() {
    const receivedCardSelect = document.getElementById('mySelectForCard');
        receivedCardSelect.disabled = false;
}
function removeCard() {
    const receivedCardSelect = document.getElementById('mySelectForCard');
    receivedCardSelect.disabled = true;

}

