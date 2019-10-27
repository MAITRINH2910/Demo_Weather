/**
 * Change Role User
 */
$(document).ready(function () {
    $("select.role").change(function () {
        var selectedRole = $(this).children("option:selected").val();
        $("#inputGroupSelect02").val(selectedRole);
        Swal.fire('Done !', 'You click OK to continue !', 'success')
        // alert("You has changed ROLE of this account is " + selectedRole);
        var id = $(this).closest("tr").attr("data-id");
        console.log(id);
        $.ajax({
            url: "/admin/change-role",
            type: "post",
            data: {
                id: id,
                role: selectedRole
            },
        })
    });
});

/**
 * Change Status
 */
$('.checkbox1').change(function() {
    // event tu uncheck -> check
    if($(this).is(":checked")) {

        swal({
            title: 'Comfirm!',
            text: "Are you sure that you want to perform this action?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#9E9E9E',
            confirmButtonText: 'Yes, change it',
            cancelButtonText: 'No, cancel'

        }).then((result) => {

            if (result.value) {
            var self = $(this);
            var id   = $(this).closest("tr").attr("data-id");

            $.ajax({
                url		:"/admin/edit-status-user",
                type	:"POST",
                data :{id : id},
            })
        }else{
            $(this).prop('checked',false)
        }
    })
    }else{

        swal({
            title: 'Comfirm!',
            text: "Are you sure that you want to perform this action?",
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#9E9E9E',
            confirmButtonText: 'Yes, change it',
            cancelButtonText: 'No, cancel'

        }).then((result) => {

            if (result.value) {
            var self = $(this);
            var id = $(this).closest("tr").attr("data-id");
            $.ajax({
                url		: "/admin/edit-status-user",
                type	: "POST",
                data:{id:id}
            })
        }else{
            $(this).prop('checked',true)
        }
    })
    }
});

/**
 * Delete Account
 */
$(".deleteAccount").click(function(){
    swal({

        title: 'Comfirm!',
        text: "Are you sure that you want to perform this action?",
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#9E9E9E',
        confirmButtonText: 'Yes, delete it',
        cancelButtonText: 'No, cancel'

    }).then((result) => {

        if (result.value) {
        var self = $(this);
        var id = $(this).closest("tr").attr("data-id");

        $.ajax({
            url	:"/admin/delete",
            type	:"POST",
            data	:{id:id},

            success: function(value){
                self.closest("tr").remove();
            },
            error: function() {
                window.location.href = "/admin?message=error_system";
            }
        })
    }
})
})