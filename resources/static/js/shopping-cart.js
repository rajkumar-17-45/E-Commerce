$(document).ready(function () {
    $(".minusButton").on("click", function (evt) {
        evt.preventDefault();
        decreaseQuantity($(this));
    });

    $(".plusButton").on("click", function (evt) {
        evt.preventDefault();
        increaseQuantity($(this));
    });
    $(".link-remove").on("click", function (evt) {
        evt.preventDefault();
        removeFromCart($(this));
    });
    updateTotal();
});

function removeFromCart(link) {
    url = link.attr("href");

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (response) {
        $("#modalTitle").text("Shopping basket");
        if (response.includes("removed")) {
            $("#myModal").on("hide.bs.modal", function (e) {
                rowNumber = link.attr("rowNumber");
                removeProduct(rowNumber);
                updateTotal();
            });
        }

        $("#modalBody").text(response);
        $("#myModal").modal();
    }).fail(function () {
        $("#modalTitle").text("Shopping basket");
        $("#modalBody").text("Error while removing product to shopping basket.");
        $("#myModal").modal();
    });
}

function removeProduct(rowNumber) {
    rowId = "row" + rowNumber;
    $("#" + rowId).remove();
}

function increaseQuantity(link) {
    productId = link.attr("pid");
    qtyInput = $("#quantity" + productId);

    newQty = parseInt(qtyInput.val()) + 1;
    if (newQty <= 10) {
        qtyInput.val(newQty);
        updateQuantity(productId, newQty);
    }
}

function decreaseQuantity(link) {
    productId = link.attr("pid");
    qtyInput = $("#quantity" + productId);

    newQty = parseInt(qtyInput.val()) - 1;
    if (newQty > 0) {
        qtyInput.val(newQty);
        updateQuantity(productId, newQty);
    }
}

function updateQuantity(productId, quantity) {
    url = contextPath + "basket/update/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function (newSubtotal) {
        updateSubtotal(newSubtotal, productId);
        updateTotal();
    }).fail(function () {
        $("#modalTitle").text("Shopping basket");
        $("#modalBody").text("Error while updating product to shopping basket.");
        $("#myModal").modal();
    });
}
function updateSubtotal(newSubtotal, productId) {
    $("#subtotal" + productId).text(newSubtotal);
    updateTotal(); // Update total whenever a subtotal changes
}

function updateTotal() {
    let total = 0.0;

    // Iterate over each productSubtotal element
    $(".productSubtotal").each(function (index, element) {
        // Extract text, remove non-numeric characters except the decimal point
        let subtotalText = $(element).text().replace(/[^0-9.]/g, '');
        let subtotal = parseFloat(subtotalText);

        if (!isNaN(subtotal)) { // Check if parsed value is a number
            total += subtotal;
        }
    });

    // Update the total value in the #totalAmount element
    $("#totalAmount").text('₹'+ total.toFixed(2)); // Format total to two decimal places and append currency symbol
}

$(document).ready(function() {
    updateTotal(); // Call initially to set the total
});
