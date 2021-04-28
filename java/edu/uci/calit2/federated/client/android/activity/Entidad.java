package edu.uci.calit2.federated.client.android.activity;
import android.graphics.drawable.Drawable;

import java.io.Serializable;
/**
 * @author Alejandro Aguilera Alcalde 2021
 */
public class Entidad implements Serializable {

    private String bien;
    private String mal;
    private String titulo_nivel; //Nivel:
    private String valor_nivel;  //Alto, Bajo, Medio
    private String nombre_app; //nombre aplicacion
    private String resumen; //resumen
    private int color; //color
    private boolean Click_bien; //
    private boolean Click_mal; //
    private boolean aux_button; //
    private String recomendacion; //resumen


    private float location;
    private float email;
    private float device;
    private float imei;
    private float serialnumber;
    private float macaddresss;
    private float advertiser;
    private Drawable foto;
    private float internal_dst;
    private float ads_dst;
    private float analytics_dst;
    private float sns_dst;
    private float develop_dst;
    private String ip;

    public Entidad(String bien,String mal, String titulo_nivel, String valor_nivel, float location, float email, float device, float imei, float serialnumber, float macaddresss, float advertiser, String nombre_app, String resumen, int color, boolean Click_bien, boolean Click_mal, boolean aux_button, Drawable foto, String recomendacion, float internal_dst,float ads_dst,float sns_dst, float develop_dst,float analytics_dst, String ip) {
       this.bien = bien;
       this.mal = mal;
       this.titulo_nivel = titulo_nivel;
       this.valor_nivel =valor_nivel;
       this.location =location;
       this.email = email;
       this.device = device;
       this.imei = imei;
       this.nombre_app = nombre_app;
       this.resumen = resumen;
       this.color = color;
       this.Click_bien = Click_bien;
       this.Click_mal = Click_mal;
       this.aux_button = aux_button;
       this.serialnumber = serialnumber;
       this.macaddresss = macaddresss;
       this.advertiser = advertiser;
       this.foto = foto;
       this.recomendacion = recomendacion;
       this.internal_dst = internal_dst;
       this.ads_dst = ads_dst;
       this.analytics_dst = analytics_dst;
       this.sns_dst = sns_dst;
       this.develop_dst = develop_dst;
       this.ip =ip;

    }

    public String getBien() {
        return bien;
    }
    public String getRecomendacion() {
        return recomendacion;
    }

    public String getMal() {
        return mal;
    }
    public Drawable getFoto() {
        return foto;
    }
    public void setFoto(Drawable foto) {
         this.foto = foto;
   }

    public String getTitulo_nivel() {
        return titulo_nivel;
    }
    public String getNombre_app() {
        return nombre_app;
    }

    public String getValor_nivel() {
        return valor_nivel;
    }
    public float getInternal_dst() {
        return internal_dst;
    }
    public float getAds_dst() {
        return ads_dst;
    }
    public float getAnalytics_dst() {
        return analytics_dst;
    }
    public float getSns_dst() {
        return sns_dst;
    }
    public float getDevelop_dst() {
        return develop_dst;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String valor) {
        this.ip = valor;
    }
    public void setInternal_dst(float valor) {
        this.internal_dst = valor;
    }
    public void setAds_dst(float valor) {
        this.ads_dst = valor;
    }
    public void setAnalytics_dst(float valor) {
        this.analytics_dst = valor;
    }
    public void setSns_dst(float valor) {
        this.sns_dst = valor;
    }
    public void setDevelop_dst(float valor) {
        this.develop_dst = valor;
    }
    public float getLocation() {
        return location;
    }
    public float getEmail() {
        return email;
    }
    public float getDevice() {
        return device;
    }
    public float getImei() {
        return imei;
    }
    public float getSerialnumber() {
        return serialnumber;
    }
    public float getMacaddresss() {
        return macaddresss;
    }
    public float getAdvertiser() {
        return advertiser;
    }

    public void setValor_nivel(String valor) {
        this.valor_nivel = valor;
    }
    public void setNombre_app(String valor) {
        this.nombre_app = valor;
    }
    public void setResumen(String valor) {
        this.resumen = valor;
    }
    public void setLocation(float valor) {
        this.location = valor;
    }
    public void setEmail(float valor) {
        this.email = valor;
    }
    public void setDevice(float valor) {
        this.device = valor;
    }
    public void setImei(float valor) {
        this.imei = valor;
    }
    public void setSerialnumber(float valor) {
        this.serialnumber = valor;
    }
    public void setMacaddresss(float valor) {
        this.macaddresss = valor;
    }
    public void setAdvertiser(float valor) {
        this.advertiser = valor;
    }
    public void setRecomendacion(String recomendacion) {
        this.recomendacion = recomendacion;
    }
    public String getResumen(){
        return resumen;
    }

    public int getColor(){
        return color;
    }

    public void setColor(int valor) {
        this.color = valor;
    }

    public boolean getClick_bien(){
        return Click_bien;
    }
    public boolean getClick_mal(){
        return Click_mal;
    }
    public boolean getAux_button(){
        return aux_button;
    }

    public void setClick_bien(boolean valor) {
        this.Click_bien = valor;
    }
    public void setClick_mal(boolean valor) {
        this.Click_mal = valor;
    }
    public void setAux_button(boolean valor) {
        this.Click_mal = valor;
    }



}
