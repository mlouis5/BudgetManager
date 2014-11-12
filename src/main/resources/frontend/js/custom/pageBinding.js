$(document).ready(function () {
    
    var userData = {
        bills: [{   billName: 'Honda',
                    payments: [{
                        paymentId: '7348bf9857eddd6b6800a0ee7f3170b1',
                        dueDate: '11-27-2014'
                    }, {
                        paymentId: '7348bf9857eddd6b6800a0ee7f3170b1',
                        dueDate: '11-27-2014'
                    }, {
                        paymentId: '7348bf9857eddd6b6800a0ee7f3170b1',
                        dueDate: '11-27-2014'
                    }]
                }, 
                {billName: 'Rent'}, {billName: 'Sallie Mae'}, {billName: 'Comcast'}, 
                {billName: 'Dept. of Edu.'}, {billName: 'PSE&G'}, {billName: 'Best Buy'}, {billName: 'Capt. One'}, {billName: 'PSE&G'}, {billName: 'Best Buy'}, {billName: 'Capt. One'}],
        currentBill: {
            billName: 'Honda',
            billId: '355C67c34E74064827B5186D8389995',
            billSource: 'Honda Financial',
            billDueDate: 27,
            billLateFeeAmount: 35.00,
            billWebsite: 'hondafinancialservices.com',
            billAmount: 400.00
        }
    };
    
    var binding = rivets.bind($('#mainContainer'), {displayData: userData});
    
    $('.nav li').click(function () {
        $(this).addClass('active')
                .siblings()
                .removeClass('active');

    });
});