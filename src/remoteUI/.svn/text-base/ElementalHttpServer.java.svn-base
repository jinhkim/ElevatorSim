package remoteUI;



import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Locale;

import mainUI.graphs.ElevatorDistanceGraph;
import mainUI.graphs.ElevatorDistanceStat;
import mainUI.graphs.ElevatorPositionGraph;
import mainUI.graphs.PassengerRideTime;
import mainUI.graphs.PassengerWaitGraph;

import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.util.EntityUtils;

import elevatorSystem.Controller;
import elevatorSystem.Passenger;
import elevatorSystem.Safety;

/**
 * Basic, yet fully functional and spec compliant, HTTP/1.1 file server.
 * <p>
 * Please note the purpose of this application is demonstrate the usage of HttpCore APIs.
 * It is NOT intended to demonstrate the most efficient way of building an HTTP file server. 
 * 
 *
 */
public class ElementalHttpServer {

	private static Controller controller;
	private static Safety safety;
	private static PassengerWaitGraph PWaitGraph;
	private static ElevatorDistanceGraph EDistanceGraph;
	private static ElevatorPositionGraph EPositionGraph;
	private static PassengerRideTime PRideTime;
	
    public static void main(String[] args) throws Exception {
    	//Thread t = new RequestListenerThread(8051, "127.0.0.1");
    	Thread t = new RequestListenerThread(Integer.valueOf(args[1]), args[0]);
        t.setDaemon(false);
        t.start();
        
    }
    
    public static void Start(String[] args, PassengerWaitGraph a, ElevatorDistanceGraph b, ElevatorPositionGraph cc, PassengerRideTime d) throws Exception {
    	//Thread t = new RequestListenerThread(8051, "127.0.0.1");
		PWaitGraph = a;
		EDistanceGraph = b;
		EPositionGraph = cc;
		PRideTime = d;
    	Thread t = new RequestListenerThread(Integer.valueOf(args[1]), args[0]);
        t.setDaemon(false);
        t.start();
    }
    
    public static void setController(Controller c) {
    	controller = c;
    }
    
    public static void setSafety(Safety s){
    	safety = s;
    }
    
    static class HttpFileHandler implements HttpRequestHandler  {
        
        private final String docRoot;
        
        public HttpFileHandler(final String docRoot) {
            super();
            this.docRoot = docRoot;
        }
        
        public void handle(
                final HttpRequest request, 
                final HttpResponse response,
                final HttpContext context) throws HttpException, IOException {
        		int chart = 0;
        		
           
        		
        String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
                throw new MethodNotSupportedException(method + " method not supported"); 
            }
            String target = request.getRequestLine().getUri();

            if (request instanceof HttpEntityEnclosingRequest) {
                HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
                
                //for(Header header: request.getAllHeaders()) {
                //    System.out.println(header.getName() + " : " + header.getValue());
                //}
                //String parameter1 = "request";
                
                
                String Params_All = EntityUtils.toString(entity);
                //System.out.println("Data:" + Params_All);
                int args_counter = 0;
                for(int i=0; i < Params_All.length(); i++)
                {
                	if(Params_All.charAt(i) == '&')
                		args_counter++;
                }
                args_counter++;
                
                String[] param = new String[args_counter];
                param = Params_All.split("&");
                
                String[][] Data = new String[args_counter][2];
                
                for(int i=0; i < args_counter; i++)
                {
                	Data[i] = param[i].split("=");
                }
                
                //i=0 is the CSRF tag
                //Printing all data received
                for(int i=1; i<args_counter; i++)
                {
                	//System.out.println(Data[i][0] + " = " + Data[i][1]);
                	
                }
                

                

                //Action
                if(Data[1][1].equals("InjectPassenger"))
                {
                	//System.out.println("The request is: Inject Passenger");
                	Passenger p = new Passenger(Integer.valueOf(Data[2][1]), Integer.valueOf(Data[3][1]), 100, false);
                	controller.newPassengerRequest(p);
                }else if(Data[1][1].equals("InjectFault")){
                	//System.out.println("The request is: Inject Fault");
                	if(Integer.valueOf(Data[3][1]) == 0)	//fault: maintin.
                	{
                		//System.out.println("MAINT.");	//Integer.valueOf(Data[2][1])
                		safety.autoMaintenanceRequest(Integer.valueOf(Data[2][1]));
                		safety.autoMaintenanceRequest(0);
                	
                	}else if(Integer.valueOf(Data[3][1]) == 1){	//fault:pass. emergency
                		safety.passengerEmergencyRequest(Integer.valueOf(Data[2][1]));
                	}else if(Integer.valueOf(Data[3][1]) == 2){ //sensor fault
                		safety.SendSafetyStatus(Integer.valueOf(Data[2][1]), true);
                	}else{
                		
                	}
                	
                	
                //info: setting up the "choose algor". controller.setalgorithm/controller.getalgorithm returns the list of algorithms	
                }else if(Data[1][1].equals("ChooseAlgorithm")){
                	controller.setAlgorithm(Integer.valueOf(Data[2][1]));
                	//System.out.println("Changing Algorithm Request");
                	
                }else if(Data[1][1].equals("Chart")){
                	chart = 1;
                	//System.out.println("Requesting Chart");

                }else{
                	//System.out.println("Unknown Request: '" + Data[1][1] + "'");
                }
                
            	//Send that data was received correctly
                response.setStatusCode(HttpStatus.SC_OK);
            }else{
            	
            
            	
            	//Send that data was received correctly
                response.setStatusCode(HttpStatus.SC_OK);
                	//ElevatorDistanceStat
                	/*
	        		try {
	        			ElevatorDistanceStat e = new ElevatorDistanceStat("GOTOSKOO" ,controller);
		        		e.Close();
	        		} catch (IOException e) {
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	        		}
	        		final File file = new File("ElevatorDistance.jpg");
	        		*/
                
	        		//PasengerWaitGraph
	        		PWaitGraph.Print();
	        		EDistanceGraph.Print();
	        		EPositionGraph.Print();
	        		PRideTime.Print();
	        		//final File file = new File("PassengerWaitGraph.jpg");
	        		  
	        		
	        		String filename = URLDecoder.decode(target).substring(1);
	        		//final File file = new File("", URLDecoder.decode(target));
	        		final File file = new File(filename);
                	if (!file.exists() || !file.canRead() || file.isDirectory())
                	{
                		//System.out.printf("No file");
                		//System.out.printf(URLDecoder.decode(target));
                	}else{
            
                		
		                FileEntity body = new FileEntity(file, "image/jpg");
		                response.setEntity(body);
		                //System.out.println("Serving file " + file.getPath());
		                
                	}
                	
                
            }
           
        }
        
    }
    
    static class RequestListenerThread extends Thread {

        private final ServerSocket serversocket;
        private final HttpParams params; 
        private final HttpService httpService;
        
        public RequestListenerThread(int port, final String docroot) throws IOException {
            this.serversocket = new ServerSocket(port);
            this.params = new SyncBasicHttpParams();
            this.params
                .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

            // Set up the HTTP protocol processor
            HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
                    new ResponseDate(),
                    new ResponseServer(),
                    new ResponseContent(),
                    new ResponseConnControl()
            });
            
            // Set up request handlers
            HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
            reqistry.register("*", new HttpFileHandler(docroot));
            
            // Set up the HTTP service
            this.httpService = new HttpService(
                    httpproc, 
                    new DefaultConnectionReuseStrategy(), 
                    new DefaultHttpResponseFactory(),
                    reqistry,
                    this.params);
        }
        
        public void run() {
            //System.out.println("Listening on port " + this.serversocket.getLocalPort());
            while (!Thread.interrupted()) {
                try {
                    // Set up HTTP connection
                    Socket socket = this.serversocket.accept();
                    DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                    //System.out.println("Incoming connection from " + socket.getInetAddress());
                    conn.bind(socket, this.params);

                    // Start worker thread
                    Thread t = new WorkerThread(this.httpService, conn);
                    t.setDaemon(true);
                    t.start();
                } catch (InterruptedIOException ex) {
                    break;
                } catch (IOException e) {
                    System.err.println("I/O error initialising connection thread: " 
                            + e.getMessage());
                    break;
                }
            }
        }
    }
    
    static class WorkerThread extends Thread {

        private final HttpService httpservice;
        private final HttpServerConnection conn;
        
        public WorkerThread(
                final HttpService httpservice, 
                final HttpServerConnection conn) {
            super();
            this.httpservice = httpservice;
            this.conn = conn;
        }
        
        public void run() {
            //System.out.println("New connection thread");
            HttpContext context = new BasicHttpContext(null);
            try {
                while (!Thread.interrupted() && this.conn.isOpen()) {
                    this.httpservice.handleRequest(this.conn, context);
                }
            } catch (ConnectionClosedException ex) {
                System.err.println("Client closed connection");
            } catch (IOException ex) {
                //System.err.println("I/O error: " + ex.getMessage());
            } catch (HttpException ex) {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            } finally {
                try {
                    this.conn.shutdown();
                } catch (IOException ignore) {}
            }
        }

    }
    
}
