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

    const $cardSelect = $('#myBalanceCardSelect');
    const $cardSelectArrow = $('#balance-card-select-arrow');
    setupSelect2($cardSelect, $cardSelectArrow);
});