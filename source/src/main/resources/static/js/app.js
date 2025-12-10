// code that i (meghan) made
(function () {
    const menuButton = document.getElementById('admin-menu-toggle');
    const overlay = document.getElementById('admin-menu-overlay');

    if (menuButton && overlay) {
        menuButton.addEventListener('click', function () {
            overlay.classList.add('admin-menu-overlay--open');
        });

        overlay.addEventListener('click', function (event) {
            if (event.target === overlay) {
                overlay.classList.remove('admin-menu-overlay--open');
            }
        });

        overlay.querySelectorAll('a').forEach(function (link) {
            link.addEventListener('click', function () {
                overlay.classList.remove('admin-menu-overlay--open');
            });
        });
    }
})();
(function () {
    /* Hamburger menu logic (same pattern as other admin pages) */
    const menuButton = document.getElementById('admin-menu-toggle');
    const overlay = document.getElementById('admin-menu-overlay');

    if (menuButton && overlay) {
        menuButton.addEventListener('click', function () {
            overlay.classList.add('admin-menu-overlay--open');
        });

        overlay.addEventListener('click', function (event) {
            if (event.target === overlay) {
                overlay.classList.remove('admin-menu-overlay--open');
            }
        });

        overlay.querySelectorAll('a').forEach(function (link) {
            link.addEventListener('click', function () {
                overlay.classList.remove('admin-menu-overlay--open');
            });
        });
    }

    const range = document.getElementById('sales-scroll-range');
    const cards = document.getElementById('admin-sales-cards');

    if (range && cards) {
        function updateRangeFromScroll() {
            const maxScroll = cards.scrollWidth - cards.clientWidth;
            if (maxScroll <= 0) {
                range.disabled = true;
                range.value = 0;
                return;
            }
            const ratio = cards.scrollLeft / maxScroll;
            range.disabled = false;
            range.value = Math.round(ratio * 100);
        }

        function updateScrollFromRange() {
            const maxScroll = cards.scrollWidth - cards.clientWidth;
            const target = (range.value / 100) * maxScroll;
            cards.scrollLeft = target;
        }

        range.addEventListener('input', updateScrollFromRange);
        cards.addEventListener('scroll', updateRangeFromScroll);
        window.addEventListener('load', updateRangeFromScroll);
        window.addEventListener('resize', updateRangeFromScroll);
    }
})();

window.addEventListener('DOMContentLoaded', function () {
    const toast = document.getElementById('cart-toast');
    if (toast) {
        setTimeout(function () {
            toast.classList.add('cart-toast-hide');
        }, 3000);
    }
});
const toast = document.getElementById('cart-toast');
if (toast) {
    setTimeout(() => {
        toast.classList.add('cart-toast-hide');
    }, 3000);
}
let currentStep = 1;

function formatMoney(number) {
    return '$' + Number(number)
        .toLocaleString('en-US', {minimumFractionDigits: 0, maximumFractionDigits: 2});
}

function setText(id, value) {
    const el = document.getElementById(id);
    if (el) el.textContent = value;
}

function updateShippingTotals() {
    const orderData = document.getElementById('order-data');
    if (!orderData) return;

    const baseSubtotal = parseFloat(orderData.dataset.subtotal || '0');
    const baseTax = parseFloat(orderData.dataset.tax || '0');

    const select = document.getElementById('shipping-method');
    let shipCost = 0;
    let methodLabel = 'Ground Shipping $0';

    if (select) {
        const val = select.value;
        if (val === 'THREE_DAY') {
            shipCost = 19;
            methodLabel = '3-Day Shipping $19';
        } else if (val === 'OVERNIGHT') {
            shipCost = 29;
            methodLabel = 'Overnight Shipping $29';
        }
    }

    const totalWithShipping = baseSubtotal + baseTax + shipCost;
    const totalNoShipping = baseSubtotal + baseTax;


    setText('items-subtotal', formatMoney(baseSubtotal));
    setText('items-tax', formatMoney(baseTax));
    setText('items-total', formatMoney(totalNoShipping));

    setText('ship-subtotal', formatMoney(baseSubtotal));
    setText('ship-tax', formatMoney(baseTax));
    setText('ship-shipping', formatMoney(shipCost));
    setText('ship-total', formatMoney(totalWithShipping));
    const shipLabel = document.getElementById('shipping-label');
    if (shipLabel) shipLabel.textContent = methodLabel;

    setText('pay-subtotal', formatMoney(baseSubtotal));
    setText('pay-tax', formatMoney(baseTax));
    setText('pay-shipping', formatMoney(shipCost));
    setText('pay-total', formatMoney(totalWithShipping));

    setText('conf-subtotal', formatMoney(baseSubtotal));
    setText('conf-tax', formatMoney(baseTax));
    setText('conf-shipping', formatMoney(shipCost));
    setText('conf-total', formatMoney(totalWithShipping));

    const confirmShipLabel = document.getElementById('confirm-shipping-label');
    if (confirmShipLabel) confirmShipLabel.textContent = methodLabel;
}

function updateStepTabs(step) {
    const pills = document.querySelectorAll('.step-pill');
    pills.forEach(pill => {
        const label = parseInt(pill.getAttribute('data-step-label'), 10);
        if (label === step) {
            pill.classList.add('active');
        } else {
            pill.classList.remove('active');
        }
    });
}

function showStep(step) {
    const steps = document.querySelectorAll('.checkout-step');
    steps.forEach(div => {
        const s = parseInt(div.getAttribute('data-step'), 10);
        div.style.display = (s === step) ? 'block' : 'none';
    });
    currentStep = step;
    updateStepTabs(step);
}

function goForwardFromStep1() {
    showStep(2);
}

function goForwardFromStep2() {
    const step2 = document.querySelector('.checkout-step[data-step="2"]');
    const inputs = step2.querySelectorAll('input[required], select[required]');
    for (const input of inputs) {
        if (!input.value || input.value.trim() === '') {
            alert('Please fill out all shipping fields before continuing.');
            input.focus();
            return;
        }
    }
    showStep(3);
}

function goForwardFromStep3() {
    const step3 = document.querySelector('.checkout-step[data-step="3"]');
    const inputs = step3.querySelectorAll('input[required]');
    for (const input of inputs) {
        if (!input.value || input.value.trim() === '') {
            alert('Please fill out all payment and billing fields before continuing.');
            input.focus();
            return;
        }
    }

    fillConfirmation();
    showStep(4);
}

function fillConfirmation() {
    const addr = document.querySelector('input[name="shipAddress"]').value;
    const addInfo = document.querySelector('input[name="shipAdditional"]').value;
    const city = document.querySelector('input[name="shipCity"]').value;
    const state = document.querySelector('input[name="shipState"]').value;
    const zip = document.querySelector('input[name="shipZip"]').value;
    const country = document.querySelector('input[name="shipCountry"]').value;

    const addressLine = addr +
        (addInfo ? (', ' + addInfo) : '') +
        ', ' + city + ', ' + state + ' ' + zip + ', ' + country;

    const addrEl = document.getElementById('confirm-address');
    if (addrEl) addrEl.textContent = addressLine;

    const cardNumber = document.getElementById('card-number').value;
    const cardExp = document.getElementById('card-exp').value;
    let last4 = '0000';
    if (cardNumber && cardNumber.replace(/\s/g, '').length >= 4) {
        const digits = cardNumber.replace(/\D/g, '');
        if (digits.length >= 4) last4 = digits.slice(-4);
    }

    const cardEl = document.getElementById('confirm-card');
    if (cardEl) cardEl.textContent = '**** **** **** ' + last4 + (cardExp ? '  •  ' + cardExp : '');

    updateShippingTotals();
}

document.addEventListener('DOMContentLoaded', () => {
    showStep(1);
    updateShippingTotals();
});

(function () {
    const menuButton = document.getElementById('admin-menu-toggle');
    const overlay = document.getElementById('admin-menu-overlay');

    if (!menuButton || !overlay) return;

    menuButton.addEventListener('click', function () {
        overlay.classList.add('admin-menu-overlay--open');
    });

    overlay.addEventListener('click', function (event) {
        if (event.target === overlay) {
            overlay.classList.remove('admin-menu-overlay--open');
        }
    });

    overlay.querySelectorAll('a').forEach(function (link) {
        link.addEventListener('click', function () {
            overlay.classList.remove('admin-menu-overlay--open');
        });
    });
})();

