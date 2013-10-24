/**
 * Created with IntelliJ IDEA.
 * User: Pedro
 * Date: 17/10/13
 * Time: 19:22
 * To change this template use File | Settings | File Templates.
 */

$(function() {
    var context = '';
    if (window.location.href.split('/')[3]) {
        context = '/' + window.location.href.split('/')[3];
    }

    function addItemToCart() {
        var item = {
            quantity: $(this).siblings().val(),
            "product.id": this.id
        };

        $.ajax({
            url: context + '/items',
            type: 'POST',
            data: item,
            dataType: 'json'
        }).always(function(jqXHR) {
                var responseText = jqXHR.responseText;
                if (responseText === "Item added to cart!") {
                    var $stock = $('#stock');
                    $stock.text(($stock.text()*1)-($('[type="number"]').val()*1));
                }
                bootbox.alert(responseText);
        });
    }

    $('.buy').click(function() {
        addItemToCart.call(this);
    });

    $('#products').on('click', 'tr', function() {
        if (this.id) {
            window.location.href = context + '/products/' + this.id;
        }
    });

    $('#updateCart').click(function() {
        var cart = {};
        cart.items = [];
        $('[type="number"]').each(function(i) {
            var item = {
                id:this.id,
                quantity:this.value
            };
            cart.items.push(item);
        });
        $.ajax({
            url: context + '/carts',
            type: 'POST',
            data: JSON.stringify(cart),
            dataType: 'json',
            contentType: 'application/json'
        }).always(function(jqXHR) {
            bootbox.alert(jqXHR.responseText, function() {
                location.reload();
            });
        });
    });

    $('.alert').fadeOut(8000);
});