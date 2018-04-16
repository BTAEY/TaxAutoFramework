/* 显示对话框，动态填充内容*/
function showDialog(modalId){
    var target = $(this);
    $("#"+modalId).on("show.bs.modal",function(event){
        var modal = $(this);
        modal.find(".modal-body").append(target);
        modal.off("show.bs.modal");
    });
    $('#'+modalId).on('hidden.bs.modal', function () {
        var modal = $(this);
        modal.data('bs.modal',null);
        modal.find(".modal-body").children().remove();
    });
    $('#'+modalId).modal();
};