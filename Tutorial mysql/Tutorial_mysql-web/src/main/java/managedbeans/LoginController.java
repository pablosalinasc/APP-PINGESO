/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbeans;

import java.security.MessageDigest;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author obi
 */
@Named(value = "loginController")
@RequestScoped
public class LoginController {

    private String correo;
    private String password;
    @Inject
    private UsuarioController userCtrl;
    
    public LoginController() {
    }
    
    @PostConstruct
    public void Init(){
        
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    public String login(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try{
            request.login(this.correo, this.password);
        }catch(ServletException e){
            System.out.println("Error al loguear");
            context.addMessage(null,new FacesMessage("Login failed."));
            System.out.println("Intento de inicio de sesión\n- Correo: "+this.correo+"\n- Password: "+this.password);
            return "/faces/error.xhtml";
        }
        return "/faces/home.xhtml";
    }
    
    public String logout(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try{
            request.logout();
            return "/faces/index.xhtml";
        }catch(ServletException e){
            context.addMessage(null,new FacesMessage("Logout failed."));
            return "/faces/home.xhtml";
        }
    }

    public boolean verifyLogin(){
        boolean verify= false;
        try {//prueba
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletRequest request = (HttpServletRequest) 
            context.getExternalContext().getRequest();//obtengo el contexto en el servidor
            
             if(request.getRemoteUser() == null){//si el usuario no esta logueado
                 verify=false;//retorna falso
             }
             else{//sino esta logueado y retorna verdadero
                 verify=true;
             }
             
        } catch (Exception e) {//si hay algun error
            verify = false;//retorna falso
        }
        return verify;
    }
    
        private String sha256(String base){
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            
            for(int i =0 ;i<hash.length;i++){
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
