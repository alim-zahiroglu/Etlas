function resizeText() {
    const cards = document.querySelectorAll('.card');

    cards.forEach(function (card) {
        const cardWidth = card.clientWidth;
        const fontSize = cardWidth / 20;
        const buttonSize = cardWidth / 25;
        const firsRowTop = cardWidth / 40;
        const firsRowLeft = cardWidth / 10;
        const firsRowRight = cardWidth / 60;

        const secondRowTop = cardWidth / 8;
        const secondRowMidTop = cardWidth / 7.2;
        const secondRowBottomTop = cardWidth / 6.4;

        const bottomRowTop = cardWidth / 5.6;
        const bottomRowLeftTop = cardWidth / 6.3;
        const bottomRowLeft = cardWidth / 14;



        card.querySelector('#first-row-left').style.top = firsRowTop + 'px';
        card.querySelector('#first-row-left').style.left = firsRowLeft + 'px';

        card.querySelector('#special-title').style.fontSize = fontSize + 'px';
        card.querySelector('#first-row-right').style.top = firsRowTop + 'px';
        card.querySelector('#first-row-right').style.right = firsRowRight + 'px';

        card.querySelector('#availableTRYLimit').style.fontSize = fontSize + 'px';
        card.querySelector('#second-row-top-right').style.right = buttonSize + 'px';
        card.querySelector('#second-row-top-right').style.top = secondRowTop + 'px';

        card.querySelector('#availableUSDLimit').style.fontSize = fontSize + 'px';
        card.querySelector('#second-row-mid-right').style.right = buttonSize + 'px';
        card.querySelector('#second-row-mid-right').style.top = secondRowMidTop + 'px';

        card.querySelector('#availableEURLimit').style.fontSize = fontSize + 'px';
        card.querySelector('#second-row-bottom-right').style.right = buttonSize + 'px';
        card.querySelector('#second-row-bottom-right').style.top = secondRowBottomTop + 'px';

        card.querySelector('#bottom-row-left').style.fontSize = fontSize + 'px';
        card.querySelector('#bottom-row-left').style.left = bottomRowLeft + 'px';
        card.querySelector('#bottom-row-left').style.top = bottomRowLeftTop + 'px';

        card.querySelector('#bottom-row-right').style.fontSize = buttonSize + 'px';
        card.querySelector('#bottom-row-right').style.right = bottomRowLeft + 'px';
        card.querySelector('#bottom-row-right').style.top = bottomRowTop + 'px';


        card.querySelectorAll('.action-buttons').forEach(function (button) {
            button.style.fontSize = buttonSize + 'px';
        });
    });
}

// Call resizeText when the window is resized
window.addEventListener('resize', resizeText);

// Call resizeText initially
resizeText();
