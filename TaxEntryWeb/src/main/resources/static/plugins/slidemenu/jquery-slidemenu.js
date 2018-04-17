(function($){
    var SlideMenu = function(ele,opt){
        this.$element = ele,
            this.defaults = {
                'url':''
            },
            this.options = $.extend({},this.defaults,opt);
    }

    SlideMenu.prototype={
        init:function(){
            $("#mini").bind('click',function(){
                if (!$('.slide-menu').hasClass('slide-menu-mini')) {
                    $('.slide-menu-item.slide-menu-show').removeClass('slide-menu-show');
                    $('.slide-menu-item').children('ul').removeAttr('style');
                    $('.slide-menu').addClass('slide-menu-mini');
                }else{
                    $('.slide-menu').removeClass('slide-menu-mini');
                }
            });
        },
        actionBinding:function(menuItemId){
            $(document).on('click','#'+menuItemId,function(){
                if (!$('.slide-menu').hasClass('slide-menu-mini')) {
                    if ($(this).next().css('display') == "none") {
                        $('.slide-menu-item').children('ul').slideUp(300);
                        $(this).next('ul').slideDown(300);
                        $(this).parent('li').addClass('slide-menu-show').siblings('li').removeClass('slide-menu-show');
                    }else{
                        $(this).next('ul').slideUp(300);
                        $('.slide-menu-item.slide-menu-show').removeClass('slide-menu-show');
                    }
                }
            });
        },
        compositeMenuTree:function (menuItems,pElem) {
            var ulBox = $(pElem).find('ul');
            for(var i=0,j=menuItems.length;i<j;i++){
                var menuItem = menuItems[i];
                var _liItem = document.createElement("li");
                var $_liItem = $(_liItem);
                $_liItem.addClass('slide-menu-item').append('<a href="javascript:;" id="'+menuItem.id+'"><i class="fas fa-briefcase slide-menu-icon"></i><span>'+menuItem.name+'</span></a>');

                this.actionBinding(menuItem.id);
                if(menuItem.hasChild){
                    $('a',$_liItem).append('<i class="fa fa-angle-right slide-menu-more"></i>');
                    $_liItem.append('<ul></ul>');
                    this.compositeMenuTree(menuItem.children,_liItem);
                    ulBox.append(_liItem);
                }else{
                    $_liItem.removeClass('slide-menu-item');
                    $('a',$_liItem).attr('href',menuItem.url);
                    $('a>i',$_liItem).removeClass('fa-briefcase').removeClass('slide-menu-icon').addClass('fa-tag');
                    ulBox.append(_liItem);
                }
            }
        },
        load: function(){
            var _self = this;
            $.ajax({
                url:this.options.url,
                dataType:'json',
                type:'GET',
                contentType:'application/json; charset=utf-8',
                success:function(data){
                    var menuItems = JSON.stringify(data);
                    console.log(menuItems);
                    _self.compositeMenuTree(data,$(_self.$element));
                }
            });
        }
    }

    $.fn.slideMenu=function(options){
        var menu = new SlideMenu(this,options);
        menu.init();
        menu.load();
    }
})(jQuery);