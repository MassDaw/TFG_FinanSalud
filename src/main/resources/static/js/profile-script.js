document.addEventListener('DOMContentLoaded', function() {
    // Card carousel functionality
    const cards = [
        {
            type: 'VISA',
            number: '1234 5678 6543 4321',
            holder: 'JACK SPARROW',
            expiry: '02/25'
        },
        {
            type: 'MASTERCARD',
            number: '5678 1234 8765 4321',
            holder: 'ANA BOMBARDINI',
            expiry: '05/26'
        }
    ];

    let currentCardIndex = 0;
    const creditCard = document.querySelector('.credit-card');
    const prevBtn = document.querySelector('.prev-btn');
    const nextBtn = document.querySelector('.next-btn');

    function updateCardDisplay() {
        const card = cards[currentCardIndex];
        document.querySelector('.credit-card-type').textContent = card.type;
        document.querySelector('.credit-card-number').textContent = card.number;
        document.querySelector('.credit-card-holder').textContent = card.holder;
        document.querySelector('.credit-card-expiry').textContent = card.expiry;

        // Change card color based on type
        if (card.type === 'VISA') {
            creditCard.style.backgroundColor = '#60a5fa';
        } else if (card.type === 'MASTERCARD') {
            creditCard.style.backgroundColor = '#f59e0b';
        }
    }

    prevBtn.addEventListener('click', function() {
        currentCardIndex = (currentCardIndex - 1 + cards.length) % cards.length;
        updateCardDisplay();
    });

    nextBtn.addEventListener('click', function() {
        currentCardIndex = (currentCardIndex + 1) % cards.length;
        updateCardDisplay();
    });

    // Initialize annual chart
    const ctx = document.getElementById('annualChart').getContext('2d');

    const monthlyData = {
        labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'],
        datasets: [
            {
                label: 'Gastos',
                data: [200, 300, 400, 200, 100, 100, 300, 500, 750, 500, 300, 200],
                borderColor: 'rgb(255, 0, 0)',
                backgroundColor: 'rgba(255, 0, 0, 0.1)',
                tension: 0.1
            },
            {
                label: 'Ingresos',
                data: [300, 400, 500, 450, 300, 400, 500, 700, 600, 400, 500, 900],
                borderColor: 'rgb(0, 200, 0)',
                backgroundColor: 'rgba(0, 200, 0, 0.1)',
                tension: 0.1
            }
        ]
    };

    const annualChart = new Chart(ctx, {
        type: 'line',
        data: monthlyData,
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                },
                x: {
                    grid: {
                        color: 'rgba(0, 0, 0, 0.1)'
                    }
                }
            },
            plugins: {
                legend: {
                    position: 'top'
                }
            }
        }
    });

    // Make sure the chart resizes when the window resizes
    window.addEventListener('resize', function() {
        annualChart.resize();
    });
});