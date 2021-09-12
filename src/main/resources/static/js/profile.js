$(document).ready(function(){

		$("#publicar").prop("disabled", true);
		
		$("#post").keyup(function(){
			
			if((($("#post").val() == "") || ($("#post").val() == null))  && (($("#img").val() == "") || ($("#img").val() == null))) $("#publicar").prop("disabled", true);

			else $("#publicar").prop("disabled", false);
		});
		
        $("#publicar").click(function(){
			
			var form = new FormData();
			
        	form.append("img", document.getElementById("img").files[0]);
        	
        	form.append("post", $("#post").val());
			
			$.ajax({
              	url: "/savePost",
             	data: form,
             	enctype: 'multipart/form-data',
             	cache: false,
             	async: false,
              	type: "POST",
              	//dataType: 'json',
             	processData: false,
             	contentType: false,
	            success: function (data) {
					
	            	cargarPostPublicado(data);
	            }
         });
		});
		
		$("#guardar").click(function(){
			
			var form = new FormData();
			
        	form.append("img-profile", document.getElementById("img-profile").files[0]);
			
			$.ajax({
              	url: "/uploadProfilePicture",
             	data: form,
             	enctype: 'multipart/form-data',
             	cache: false,
             	async: false,
              	type: "POST",
              	//dataType: 'json',
             	processData: false,
             	contentType: false,
	            success: function (data) {
	            	if(data) window.location.reload();    
	            }
        	});
		});
        
        
        $("#img").change(function() {
	        
	        if(! validatePicture(this)){
	            alert('Seleccione una imagen válida (JPEG/JPG/GIF/TIFF).');
	            $("#img").val('');
	            return false;
	        }
	        
	        $("#publicar").prop("disabled", false);
		});
		
		$("#img-profile").change(function(){
 
	        if(! validatePicture(this)){
	            alert('Seleccione una imagen válida (JPEG/JPG/GIF/TIFF).');
	            $("#img-profile").val('');
	            return false;
	        }

	        
	        
		});
		
		function validatePicture(data){
			
			var file = data.files[0];
	        var imagefile = file.type;
	        var match= ["image/jpeg","image/jpg","image/gif","image/tiff"];
	        if(!((imagefile==match[0]) || (imagefile==match[1]) || (imagefile==match[2]) || (imagefile==match[3]) || (imagefile==match[4]))){
	            
	            return false;
	        }
			return true;
		}
		
		$(".btn-outline-dark").each(function(){
			//if($(this).attr("data-idPost") == 1) alert("1");
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
						comentario : text
					}
					$.post("/guardarComentario", datos, function(data){
						$(".overflow-auto[data-idPost='"+idPost+"']").find(".card").each(function(index){
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
        
        $(document).on("click", ".dropdown-item", function(){
			
			var idPost = {
				idPost : $(this).attr("data-idPost")
			}
			
			var id = $(this).attr("data-idPost");
			
			if($(this).text() == "Eliminar"){
				
				$.post("/eliminarPost", idPost);

				$(".rounded-cus[data-idPost='"+id+"']").remove();
			}else{
				
				var contenido = $(".rounded-cus[data-idPost='"+id+"']").find("p.text-center").text();
				
				var foto = $(".rounded-cus[data-idPost='"+id+"']").find("img.w-75").attr("src");
				
				$("#contenidoEditar").text(contenido);
				
				$("#divFoto").children().remove();
				
				$("#eliminarImagen").remove();
				
				if(foto != null){
					$("#divFoto").html("<img id='imgPost' src='"+foto+"' class='w-50'>");
					$("#divFoto").after("<button id='eliminarImagen' class='btn btn-outline-info w-100 mt-2'>Eliminar imagen</button>");
				}else{
					$("#divFoto").html("<input type='file' class='btn btn-success ms-3 mt-2 w-100' id='img-editar' value='Foto'>");
				}
				
				$("#guardarModificacion").attr("data-idPost", id);
				
				$("#modalEditarPost").modal("show");
			}
		
			return false;
		});
		
		$(document).on("click", "#eliminarImagen", function(){ 
			$("#imgPost").remove(); 
			$("#eliminarImagen").remove();
			$("#divFoto").html("<input type='file' class='btn btn-success ms-3 mt-2 w-100' id='img-editar' value='Foto'>");
		});
		
		$("#guardarModificacion").click(function(){
			
			var id = $(this).attr("data-idPost");
			
			var post = {
				idPost : id,
				contenido : $("#contenidoEditar").val(),
				imagen : $("#imgPost").attr("src")				
			}
			
			$.post("/editarPost", post, function(data){
				if(data == "postEliminado"){
					alert("El post quedò vacio asi que lo eliminamos");
				}else{
					
					$(".rounded-cus[data-idPost='"+id+"']").find("p.text-center").text($("#contenidoEditar").val());
					
					if($("#imgPost").attr("src") == null)
						$(".rounded-cus[data-idPost='"+id+"']").find("img.w-75").remove();
				}
				
				$("#modalEditarPost").modal("hide");
			});
		})
		
		
		
    });//FIN DEL DOCUMENT READY
    
    function enviarEach(element, idPost){
		
		$.post("/verificationLikePost", idPost, function(data){
			if(data){
				$(element).addClass("active");
			}
				
		});
	}
	
	function cargarPostPublicado(data){
		var idPost = {
			idPost : data
		}
		
		$.post("/buscarPost", idPost, function(post){
			$("#img").val('');
			$("#post").val('');
			$("#divAux").after(post);
		})
	}
	