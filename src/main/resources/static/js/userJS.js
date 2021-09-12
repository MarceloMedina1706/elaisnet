$(document).ready(function(){
	$(".btn-outline-dark").each(function(){
		var idPost = {
			idPost : $(this).attr("data-idPost")
		};
		enviarEach($(this), idPost);
	});
	
	$(document).on("click", ".btn-outline-dark", function(){
		
		var idPost = {
			idPost : $(this).attr("data-idPost")
		}
				
		if(!($(this).hasClass("active"))){
					
			$.post("/likePost", idPost);
					
			$(this).addClass("active");
					
			var alerta = $(this).find("span").text();
					
			var numero = parseInt(alerta);
					
			numero += 1;
					
			$(this).find("span").text(numero);
		}else{
					
			$.post("/unLikePost", idPost);
					
			$(this).removeClass("active");
					
			var alerta = $(this).find("span").text();
					
			var numero = parseInt(alerta);
					
			numero -= 1;
					
			$(this).find("span").text(numero);	
		}		
				
	});
	
	$(".btn-outline-secondary").click(function(){
		var sPageURL = window.location.pathname;
		sPageURL = sPageURL.split("/");
		var idUserFollow = {
			idUserFollow: sPageURL[2]
		}
		
		if(!$(this).hasClass("active")){
			$(this).text("Siguiendo");
			$(this).addClass("active");
			
			$.post("/seguir", idUserFollow, function(){
				window.location.reload();
			});
		}else{
			$(this).text("Seguir");
			$(this).removeClass("active");
			$.post("/dejarDeSeguir", idUserFollow, function(){
				window.location.reload();
			});
		}

	});
	
	// COMENTARIOS =================================================================================================================================================================================
		var cont = 0, cont2=0;
		$(document).on("click", ".btn-outline-danger", function(){
			cont++;
			$("#modalComentario").modal("show");
			var idPost = $(this).attr("data-idPost");
			$("#guardarComentario").attr("data-post", idPost);
			$("#guardarComentario").click(function(){
				
				cont2++;
				var text = $("#comentario").val();
				$("#modalComentario").modal("hide");
				$("#comentario").val('');
				if(cont == cont2){
					 var datos = {
						idPost : $("#guardarComentario").attr("data-post"),
						comentario : text,
						userBoolean:true
					}
					
					$.post("/guardarComentario", datos, function(data){
						var post = $("#guardarComentario").attr("data-post");
						
						$(".overflow-auto[data-idPost='"+post+"']").find(".card").each(function(index){
							console.log(post);
							if(index == 0){
								$(this).before(data);
								if($(this).find("p").text() == "No hay comentarios"){
									$(this).remove();
								}
								return;
							}
						});					 
						
					 });
					
				}else{
					cont2 = cont;
				}
			});
			
		});
		cont = cont2 = 0;
// COMENTARIOS =================================================================================================================================================================================			

	
	// COMENTARIOS =================================================================================================================================================================================			
		$(document).on("click", ".btn-outline-primary", function(){

            var id = $(this).attr("data-idPost");
            var selector = ".overflow-auto[data-idPost='"+id+"']"
            if($(selector).find(".collapse").is(":visible") == false){
                if($(selector).find(".collapse").children().length == 0){
					$(selector).find(".collapse").html('<div class="card card-body"><div class="bg-warning p-4 rounded-1em"><p>No hay comentarios</p></div></div>');
				}
                $(selector).find(".collapse").fadeToggle("show");
                $(selector).css("height", "16em");
            }else{

                $(selector).find(".collapse").fadeToggle("hide");
                $(selector).css("height", ".1em");
                
            }
        });
        
        // COMENTARIOS =================================================================================================================================================================================
});//FIN READY


function enviarEach(element, idPost){
	$.post("/verificationLikePost", idPost, function(data){
		if(data){
			$(element).addClass("active");
		}
				
	});
}