function resizeText() {
    const cards = document.querySelectorAll('.card');

    cards.forEach(function (card) {
        const cardWidth = card.clientWidth;
        const fontSize = cardWidth / 20;
        const buttonSize = cardWidth / 25;
        const firsRowTop = cardWidth / 40;
        const firsRowLeft = cardWidth / 10;
        const firsRowRight = cardWidth / 60;

        const secondRowFontSize = cardWidth / 17;
        const secondRowTop = cardWidth / 5;

        const bottomRowTop = cardWidth / 3.8;
        const bottomRowLeft = cardWidth / 12;

        const bottomRowRightTop = cardWidth / 3.4;
        const bottomRowRight = cardWidth / 12;


        card.querySelector('#first-row-left').style.top = firsRowTop + 'px';
        card.querySelector('#first-row-left').style.left = firsRowLeft + 'px';

        card.querySelector('#special-title').style.fontSize = fontSize + 'px';
        card.querySelector('#first-row-right').style.top = firsRowTop + 'px';
        card.querySelector('#first-row-right').style.right = firsRowRight + 'px';

        card.querySelector('#availableLimit').style.fontSize = secondRowFontSize + 'px';
        card.querySelector('#second-row-right').style.right = buttonSize + 'px';
        card.querySelector('#second-row-right').style.top = secondRowTop + 'px';

        card.querySelector('#bottom-row-left').style.fontSize = fontSize + 'px';
        card.querySelector('#bottom-row-left').style.left = bottomRowLeft + 'px';
        card.querySelector('#bottom-row-left').style.top = bottomRowTop + 'px';

        card.querySelector('#bottom-row-right').style.fontSize = fontSize + 'px';
        card.querySelector('#bottom-row-right').style.right = bottomRowRight + 'px';
        card.querySelector('#bottom-row-right').style.top = bottomRowRightTop + 'px';


        card.querySelectorAll('.action-buttons').forEach(function (button) {
            button.style.fontSize = buttonSize + 'px';
        });
    });
}

// Call resizeText when the window is resized
window.addEventListener('resize', resizeText);

// Call resizeText initially
resizeText();
