package com.elaisnet.core.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elaisnet.core.model.Comentario;
import com.elaisnet.core.model.Follow;
import com.elaisnet.core.model.LikePost;
import com.elaisnet.core.model.Post;
import com.elaisnet.core.model.User;
import com.elaisnet.core.service.ComentarioService;
import com.elaisnet.core.service.FollowService;
import com.elaisnet.core.service.LikePostService;
import com.elaisnet.core.service.PostService;
import com.elaisnet.core.service.UserService;

@Controller
public class UserProfileController {
	
	@GetMapping("/homepage")
	public ModelAndView homepage(HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView("/homepage");
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		List<Follow> follows = followService.getFollowByIdUserFollower(user);
		
		List<Long> ids = new ArrayList<Long>();
		
		for(Follow f : follows)
			ids.add(f.getIdUser().getIdUser());
		
		List<Post> posts = postService.getPostFollow(user.getIdUser(), ids);
		
		mav.addObject("profilePicture", user.getProfilePicture());
		
		mav.addObject("posts", posts);
		
		mav.addObject("myId", user.getIdUser());
		
		return mav;
	}
	
	@GetMapping("/userProfile")
	public ModelAndView userProfile(HttpServletRequest request) {
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		String username = user.getName() + " " + user.getLastName();
		
		ModelAndView mav = new ModelAndView("/userProfile");
		
		mav.addObject("username", username);
		
		List<Post> posts = postService.getPostsByUser(user);
		
		if(!posts.isEmpty()) {
			
			mav.addObject("posts", posts);
		}
		
		mav.addObject("emailUser", user.getEmail());
		
		mav.addObject("profilePicture", user.getProfilePicture());
		
		return mav;
	}
	
	@PostMapping("/savePost")
	public @ResponseBody long savePost(@RequestParam(value="post", required=false) String textPost,
			@RequestParam(name="img", required=false) MultipartFile img,
			HttpServletRequest request) {
		
		Post post = new Post();
		
		if(img != null) {
			
			if(!img.isEmpty()) {
				
				Path directorioImagenes = Paths.get("src//main//resources//static/img");
				
				String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
				
				try {
					byte byteImg[] = img.getBytes();
					
					Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + img.getOriginalFilename());
					
					Files.write(rutaCompleta, byteImg);
					
					post.setFoto(img.getOriginalFilename());

				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		
		post.setContenido(textPost);
		
		Calendar cal = Calendar.getInstance();
    	
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        
    	Date date = new Date(cal.getTime().getTime());
    	
    	post.setFecha(date);
    	
    	post.setIdUser(userService.getUserByEmail((String)request.getSession(false).getAttribute("user")));
    	
		return postService.savePost(post);
	}
	
	@PostMapping("/uploadProfilePicture")
	public @ResponseBody boolean uploadProfilePicture(@RequestParam("img-profile") MultipartFile img,
			HttpServletRequest request){
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		if(!img.isEmpty()) {
			
			String path = "src//main//resources//static//img/" + user.getEmail();
			
			Path directorioImagenes = Paths.get(path);
			
			String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
			
			try {
				byte byteImg[] = img.getBytes();
				
				Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + img.getOriginalFilename());
				
				Files.write(rutaCompleta, byteImg);

			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		String rutaImg = "img/" + user.getEmail() + "/" + img.getOriginalFilename();
		
		userService.updateProfilePicture(user, rutaImg);
		
		return true;
	}
	
	@PostMapping("/verificationLikePost")
	public @ResponseBody boolean veritficationLikePost(@RequestParam int idPost,
			HttpServletRequest request) {
		
		Post post = postService.getPostById(idPost);
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		List<LikePost> likes = post.getLikesPost();
		
		for(LikePost lp : likes) {
			if(lp.getIdUser().getIdUser() == user.getIdUser()) {
				return true;
			}
		}
		
		return false;
	}
	
	@PostMapping("/likePost")
	public @ResponseBody boolean likePost(@RequestParam int idPost,
			HttpServletRequest request) {
		
		Post post = postService.getPostById(idPost);
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		LikePost like = new LikePost();
		
		like.setIdPost(post);
		
		like.setIdUser(user);
		
		likePostService.saveLikePost(like);
		
		return true;
	}
	
	@PostMapping("/unLikePost")
	public @ResponseBody boolean unLikePost(@RequestParam int idPost,
			HttpServletRequest request) {
		
		Post post = postService.getPostById(idPost);
		
		likePostService.deleteLikePost(post);
		
		return true;
	}
	
	@PostMapping("/buscarPost")
	public @ResponseBody String buscarPost(@RequestParam long idPost) {
		
		Post post = postService.getPostById(idPost);
		
		User user = post.getIdUser();
		
		String username = user.getName() + " " + user.getLastName();
		
		String fotoPost = "";
		
		if(post.getFoto() != null) {
			
			fotoPost = "<div class=\"d-flex justify-content-center\">"
					+ "            <img src='img/"+user.getEmail()+"/"+post.getFoto()+"' class='w-75'>"
					+ "        </div>";
		}
		
		String html = "<div class='d-flex justify-content-center mb-2'>"
				+ "        <div class=\"rounded-cus shadow w-50 mt-3\" data-idPost=\""+post.getIdPost()+"\">"
				+ "            <div class=\"d-flex justify-content-center\">"
				+ "                <div class=\"p-3 d-flex justify-content-center\">"
				+ "                    <img src=\""+user.getProfilePicture()+ "\" class=\"w-25\">"
				+ "                   <div class=\"w-40 ms-3\">"
				+ "                       <div class=\"d-flex align-self-center ms-4\">"
				+ "                          <p class=\"fs-4\">"+username+"</p>"
				+ "                       </div>"
				+ "                       <div class=\"ms-4\">"
				+ "                           <p class=\"fs-5\">"+post.getFecha()+"</p>"
				+ "                       </div>"
				+ "                   </div>"
				+ "					  <div>"
				+ "                   		<div class=\"nav-item dropdown\">"
				+ "						    <a class=\"nav-link dropdown-toggle\" data-bs-toggle=\"dropdown\" href=\"#\" role=\"button\" aria-expanded=\"false\"></a>"
				+ "						    <ul class=\"dropdown-menu\">"
				+ "						      <li><a class=\"dropdown-item\" data-idPost=\""+post.getIdPost()+"\">Editar</a></li>"
				+ "						      <li><a class=\"dropdown-item\" data-idPost=\""+post.getIdPost()+"\">Eliminar</a></li>"
				+ "						    </ul>"
				+ "						 </div>"
				+ "                   </div>"
				+ "               </div>"
				+ "            </div>"
				+ "            <div class=\"px-4 mb-2 border-bottom\">"
				+ "                <p class=\"fs-5 text-center\">"+post.getContenido()+"</p>"
				+ 					fotoPost
				+ "            </div>"
				+ "            <div class=\"d-flex px-3 mb-2\">"
				+ "            	<button class=\"btn btn-outline-dark w-50 fs-5 shadow-none\" data-idPost=\""+post.getIdPost()+"\">"
				+ "					Me gusta <span>"+post.getLikesPost().size()+"</span>"
				+ "				</button>"
				+ "	            <button class=\"btn btn-outline-danger w-50 fs-5 shadow-none\" data-idPost=\""+post.getIdPost()+"\">Comentar</button>"
				+ "            </div>\r\n"
				+ "			<div class=\"modal fade\" id=\"modalComentario\" aria-labelledby=\"modalLabel2\" aria-hidden=\"true\">"
				+ "			  <div class=\"modal-dialog\">"
				+ "			    <div class=\"modal-content\">"
				+ "			      <div class=\"modal-header\">"
				+ "			        <h5 class=\"modal-title\" id=\"modalLabel2\">Comentario.</h5>"
				+ "			        <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>"
				+ "			      </div>"
				+ "			      <div class=\"modal-body\">"
				+ "			      		<textarea class=\"form-control\" placeholder=\"Deja un comentario aqui\" id=\"comentario\"></textarea>"
				+ "			      </div>\r\n"
				+ "			      <div class=\"modal-footer\">"
				+ "			        <button type=\"button\" class=\"btn btn-secondary\" data-bs-dismiss=\"modal\">Cerrar</button>"
				+ "			        <button class=\"btn btn-success\">Guardar</button>"
				+ "			      </div>"
				+ "			    </div>"
				+ "			  </div>"
				+ "			</div>"
				+ "        </div>"
				+ "	</div>";
		
		
		return html;
	}
	
	@PostMapping("/guardarComentario")
	public @ResponseBody String guardarComentario(@RequestParam long idPost, 
			@RequestParam String comentario,
			@RequestParam(value="userBoolean", required=false) boolean userBoolean,
			HttpServletRequest request){
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		Post post = postService.getPostById(idPost);
		
		Calendar cal = Calendar.getInstance();
    	
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        
    	Date date = new Date(cal.getTime().getTime());
    	
    	Comentario comentarioPost = new Comentario();
    	
    	comentarioPost.setComentario(comentario);
    	
    	comentarioPost.setFechaComentario(date);
    	
    	comentarioPost.setIdPost(post);
    	
    	comentarioPost.setIdUser(user);
    	
    	if(userBoolean) {
    		return buscarComentario(comentarioService.saveComentario(comentarioPost), true);
    	}
    	
    	return buscarComentario(comentarioService.saveComentario(comentarioPost), false);
	}
	
	private String buscarComentario(long idComentario, boolean userBoolean) {
		
		Comentario comentario = comentarioService.getComentarioById(idComentario);
		
		String username = comentario.getIdUser().getName() + " " + comentario.getIdUser().getLastName();
		
		if(userBoolean) {
			return "<div class=\"card card-body\">"
					+ "		<div class=\"bg-warning p-4 rounded-1em\">"
					+ "         <div class=\"d-flex align-items-end\">"
					+ "              <img src='../"+comentario.getIdUser().getProfilePicture()+"' class=\"w-10 \">"
					+ "                   <span class=\"fs-4 text-end mx-3\">"+username+"</span>"
					+ "                   <small>"+comentario.getFechaComentario()+"</small>"
					+ "         </div>"
					+ "         <div class=\"border-top mt-2 p-2\">"
					+ "              <p>"+comentario.getComentario()+"</p>"
					+ "         </div>"
					+ " 	</div>"
					+ "	</div>";
		}
		
		String html = "<div class=\"card card-body\">"
				+ "		<div class=\"bg-warning p-4 rounded-1em\">"
				+ "         <div class=\"d-flex align-items-end\">"
				+ "              <img src='"+comentario.getIdUser().getProfilePicture()+"' class=\"w-10 \">"
				+ "                   <span class=\"fs-4 text-end mx-3\">"+username+"</span>"
				+ "                   <small>"+comentario.getFechaComentario()+"</small>"
				+ "         </div>"
				+ "         <div class=\"border-top mt-2 p-2\">"
				+ "              <p>"+comentario.getComentario()+"</p>"
				+ "         </div>"
				+ " 	</div>"
				+ "	</div>";
		
		return html;
	}
	
	@PostMapping("/eliminarPost")
	public @ResponseBody void eliminarPost(@RequestParam long idPost){
		
		postService.deletePostByIdPost(idPost);
		
	}
	
	@PostMapping("/editarPost")
	public @ResponseBody String editarPost(@RequestParam long idPost, 
			@RequestParam(value="contenido", required=false) String contenido,
			@RequestParam(value="imagen", required=false) String imagen) {
		
		if((contenido.isBlank()) && (imagen == null)) {
			return "postEliminado";
		}
		
		Post post = postService.getPostById(idPost);
		
		post.setContenido(contenido);
		
		post.setFoto(imagen);
		
		postService.savePost(post);
		
		return "success";
	}
	
	//==================================================================================================================================================
	
	@GetMapping("/user/{idUser}")
	public ModelAndView user(@PathVariable("idUser") long idUser,
			HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("/user");
		
		User user = userService.getUserByIdUser(idUser);
		
		User userFollower = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		if(idUser == userFollower.getIdUser()) return new ModelAndView("redirect:/userProfile");
		
		String profilePicture = user.getProfilePicture();
		
		String username = user.getName() + " " + user.getLastName();
		
		String emailUser = user.getEmail();
		
		mav.addObject("profilePicture", profilePicture);
		
		mav.addObject("username", username);
		
		mav.addObject("emailUser", emailUser);
		
		List<Post> posts = postService.getPostsByUser(user);
		
		mav.addObject("posts", posts);
		
		Follow f = followService.findFollow(user, userFollower);
		
		boolean check = false;
		
		if(f != null) check = true;
		
		mav.addObject("follow", check);
		
		return mav;
	}
	
	@PostMapping("/seguir")
	public @ResponseBody void seguir(@RequestParam long idUserFollow, 
			HttpServletRequest request) {
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		User userFollow = userService.getUserByIdUser(idUserFollow);
		
		Follow follow = new Follow();
		
		follow.setIdUser(userFollow);
		
		follow.setIdUserFollower(user);
		
		followService.saveFollow(follow);
		
	}
	
	@PostMapping("/dejarDeSeguir")
	public @ResponseBody void dejarDeSeguir(@RequestParam long idUserFollow, 
			HttpServletRequest request) {
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		User userFollow = userService.getUserByIdUser(idUserFollow);
		
		Follow follow = followService.findFollow(userFollow, user);
		
		followService.deleteFollow(follow);
		
	}
	
	//==================================================================================================================================================
	
	@GetMapping("/followers")
	public ModelAndView verFollowers(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView("/followers");
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		String username = user.getName() + " " + user.getLastName();
		
		mav.addObject("username", username);
		
		mav.addObject("profilePicture", user.getProfilePicture());
		
		return mav;
	}
	
	@PostMapping("/buscarFollow")
	public @ResponseBody String buscarFolow(HttpServletRequest request, @RequestParam int tipo ) {
		
		User user = userService.getUserByEmail((String)request.getSession(false).getAttribute("user"));
		
		String html = "";
		
		if(tipo == 0) {//SEGUIDORES
			
			List<Follow> myFollowers = followService.getFollowByIdUser(user);
			
			if(myFollowers.isEmpty()) {
				html = "<p class='fs-4 bg-warning mt-1'>Sin seguidores</p>";
				return html;
			}
			
			for(Follow f : myFollowers) {
				html += "<div class='fs-4 mt-1'>"
						+ "<img src='"+f.getIdUserFollower().getProfilePicture()+"' class='w-10'>"
						+ "<a href='#' data-idUser='"+f.getIdUserFollower().getIdUser()+"' class='text-decoration-none text-dark ms-4'>"
						+ f.getIdUserFollower().getName()+" "+f.getIdUserFollower().getLastName()+"</a></div>";
			}
			
			return html;
		}
		
		//SEGUIDOS
		List<Follow> myFollowed = followService.getFollowByIdUserFollower(user);
		
		if(myFollowed.isEmpty()) {
			html = "<p class='fs-4 bg-warning mt-1'>Sin seguidos</p>";
			return html;
		}
		
		for(Follow f : myFollowed) {
			html += "<div class='fs-4 mt-1'>"
					+ "<img src='"+f.getIdUser().getProfilePicture()+"' class='w-10'>"
					+ "<a href='#' data-idUser='"+f.getIdUser().getIdUser()+"' class='text-decoration-none text-dark ms-4'>"
					+ f.getIdUser().getName()+" "+f.getIdUser().getLastName()+"</a></div>";
		}
		
		return html;
	}
	
	
	
	//==================================================================================================================================================
	
	@GetMapping("/resultado")
	public ModelAndView resultado(@RequestParam String search) {
		
		ModelAndView mav = new ModelAndView("/resultado");
		
		HashSet<User> res = userService.getSearch(search);
		
		if(res.isEmpty()) {
			
			mav.addObject("empty", true);	
		}else {
			
			mav.addObject("resultados", res);
		}
		
		return mav;
	}
	
	
	
	//==================================================================================================================================================
	@Autowired
    private UserService userService;
	
	@Autowired
    private PostService postService;
	
	@Autowired
    private LikePostService likePostService;
	
	@Autowired
    private ComentarioService comentarioService;
	
	@Autowired
	private FollowService followService;
}
