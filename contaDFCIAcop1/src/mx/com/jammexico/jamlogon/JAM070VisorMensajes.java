package mx.com.jammexico.jamlogon;

public enum JAM070VisorMensajes
{
  LINEA01(1, "Inicializando Medios de Seguridad (Protocolo de Encapsulamiento de datos)", 3),  LINEA02(12, "Definiendo valores por defecto y parametros de conexion", 3),  LINEA03(20, "Iniciando ambiente de operacion virtual y enlace persistente", 0),  LINEA04(30, "Enlazandose al Servidor de Negocios y directivas transaccionales", 0),  LINEA05(40, "Recuperando Informacion corporativa via enlace remoto", 0),  LINEA06(50, "Configurando escritorio y perfil usuario. Seguridad levantada con exito", 0),  LINEA07(60, "Transfiriendo Nucleo del Servidor al cliente y levantando servicios remotos", 0),  LINEA08(70, "Prepara enlace definitivo y estabiliza conexion permanente", 0),  LINEA09(80, "Recibiendo Nucleo del Cliente y levantando servicios locales", 0),  LINEA10(90, "Carga Menu del Usuario y fecha del sistema", 0),  LINEA11(100, "Asigna perfil de seguridad", 0),  LINEA12(110, "Verifica Funciones previamente abiertas", 0),  LINEA13(120, "Descarga Funciones del Usuario", 3),  LINEA14(130, "Finaliza vuelco de Memoria hacia el Cliente", 3),  ERROR04(30, "Error al Validar Usuario DBSYSTEM : ", 0),  ERROR05(40, "Error al conectarse a las bases Alternativas@2@ :", 0),  ERROR07(60, "El Usuario No Tiene Asignado un Menu", 0),  ERROR08(70, "Error al Recuperar el Menu de Usuario : ", 0),  ERROR13(120, "Error al Grabar archivo de configuracion : ", 0),  ERROR14(130, "ERROR, al Recuperar el Trace de las funciones abiertas. ", 0);
  
  private int linea;
  private int waiting;
  private String mensaje;
  public static final int MAX_LINEA01_WAIT = 9;
  public static final int MAX_LINEA_MENSAJES = 130;
  
  public static void main(String[] args) {}
  
  private JAM070VisorMensajes(int linea)
  {
    this.linea = linea;
  }
  
  private JAM070VisorMensajes(String mensaje)
  {
    this.mensaje = mensaje;
  }
  
  private JAM070VisorMensajes(int linea, String mensaje, int waiting)
  {
    this.linea = linea;
    this.mensaje = mensaje;
    this.waiting = waiting;
  }
  
  private String setErrorMensaje(String argError)
  {
    return argError;
  }
  
  public int getLinea()
  {
    return this.linea;
  }
  
  public String getMensaje()
  {
    return this.mensaje;
  }
  
  public int getWaiting()
  {
    return this.waiting;
  }
}
