
import java.awt.*; 
import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.List;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.Timer;


// Xử lý sự kiên

public class JEventQueue implements 
      ActionListener,AWTEventListener,KeyListener,DocumentListener,ChangeListener,
      ListSelectionListener, TableModelListener,MouseListener,WindowListener,
      WindowFocusListener,WindowStateListener,FocusListener,MenuKeyListener,
      ComponentListener{
   //------------------------------------------------
   private  Queue<EventObject> queue = new LinkedList<EventObject>();

  
   public synchronized void addEvent(EventObject e){ 
      queue.offer(e);  notify();
   }
   
   public synchronized void removeEvents(String nm){
     Object o=null;
     for(EventObject e:queue){
       String n=getName(e);
       if(nm.equals(n)){o=e;break;}
     }

     if(o!=null){
       queue.remove(o);
       removeEvents(nm);
     }   
   }

   public synchronized boolean hasEvent(){return queue.peek()!=null;}

   public synchronized EventObject peekEvent(){return queue.peek();}
 
   public synchronized EventObject waitEvent(){
     EventObject r=null;
     for(;;){
        r=queue.poll();
        if(r!=null)return r;
        try{
          wait();
        }catch(Exception e){return null;}
     }
   }

   //  nghe các sự kiện 
   
   public void actionPerformed(ActionEvent e){ addEvent(e); }
  
   public void changedUpdate(DocumentEvent e){ addEvent(new DocumentEventObject(e)); } 
   
   public void insertUpdate(DocumentEvent e){ addEvent(new DocumentEventObject(e)); } 
  
   public void removeUpdate(DocumentEvent e){ addEvent(new DocumentEventObject(e)); } 

   
   public void keyReleased(KeyEvent e){ addEvent(e); }
  
   public void keyPressed(KeyEvent e){ addEvent(e); }
   
   public void keyTyped(KeyEvent e){ addEvent(e); }

   
   public void valueChanged(ListSelectionEvent e){ addEvent(e); }
   public void tableChanged(TableModelEvent e){ addEvent(e); }  

  
   public void stateChanged(ChangeEvent e){
      Object o=e.getSource();
      if(o instanceof JSlider){if(((JSlider)o).getValueIsAdjusting())return;}
      addEvent(e); }

 
   public void eventDispatched(AWTEvent e){ addEvent(e); }


   public void mouseClicked(MouseEvent e){addEvent(e);}
   
   public void mouseEntered(MouseEvent e){addEvent(e);}
   
   public void mouseExited(MouseEvent e){addEvent(e);}
  
   public void mousePressed(MouseEvent e){addEvent(e);}
   
   public void mouseReleased(MouseEvent e){addEvent(e);}
  
   public void windowActivated(WindowEvent e){addEvent(e);} 
 
   public void windowClosed(WindowEvent e){addEvent(e);} 
   
   public void windowClosing(WindowEvent e){addEvent(e);} 

   public void windowDeactivated(WindowEvent e){addEvent(e);} 
   
   public void windowDeiconified(WindowEvent e){addEvent(e);} 
  
   public void windowIconified(WindowEvent e){addEvent(e);} 
   
   public void windowOpened(WindowEvent e){addEvent(e);} 

   public void windowGainedFocus(WindowEvent e){addEvent(e);}  

   public void windowLostFocus(WindowEvent e){addEvent(e);}  
 
   public void windowStateChanged(WindowEvent e){addEvent(e);}  

  
   public void componentHidden(ComponentEvent e) {addEvent(e);} 
        
   public void componentMoved(ComponentEvent e) {addEvent(e);} 
          
   public void componentResized(ComponentEvent e) {addEvent(e);} 
        
   public void componentShown(ComponentEvent e) {addEvent(e);} 
   
   public void focusGained(FocusEvent e){addEvent(e);}   
          
   public void focusLost(FocusEvent e){addEvent(e);}   
        
   public void menuKeyPressed(MenuKeyEvent e) {addEvent(e);}
         
   public void menuKeyReleased(MenuKeyEvent e) {addEvent(e);}
          
   public void menuKeyTyped(MenuKeyEvent e) {addEvent(e);}
         
   public static String eventType(EventObject o){
    if(o==null)return "null";
    String s= o.getClass().getName();
    if(s.indexOf(".")>0)return s.substring(s.lastIndexOf(".")+1);
    return s;  
   }
  private static Map<Object,String> names=new HashMap<Object,String>();
  public static String getName(EventObject e){
     Object o=e.getSource();
     if(o instanceof JComponent){
        Object v=((JComponent) o).getClientProperty("name");
        if(v!=null)return (String)v;
     } else
     if(o instanceof Document){
        Object v=((Document) o).getProperty("name");
        if(v!=null)return (String)v;
     } else
     if(o instanceof Component){
        Object v=((Component) o).getName();
        if(v!=null)return (String)v;
     }  else
     if(names.containsKey(o)){return names.get(o);}
     String s=o.toString();
     if(s.length()<20&&s.indexOf("[")<0)return s;
     return "";
   }

   public static void addName(Object o,String nm){
     if(o instanceof JComponent){
        ((JComponent) o).putClientProperty("name",nm);
     } else
     if(o instanceof Document){
        ((Document) o).putProperty("name",nm);
     } else
     if(o instanceof Component){
        ((Component) o).setName(nm);
     }else{
        names.put(o,nm);
     }
   }

   public void listenTo(Component jc,String s){
     addName(jc,s);
     jc.addKeyListener(this);
     if(jc instanceof JMenuItem){
       ((JMenuItem) jc).addActionListener(this);
     } else
     if(jc instanceof AbstractButton){
       ((AbstractButton) jc).addActionListener(this);
     }else
     if(jc instanceof JComboBox){
       ((JComboBox) jc).addActionListener(this);
     }else
     if(jc instanceof JSlider){
       ((JSlider) jc).addChangeListener(this);
     }else
     if(jc instanceof JSpinner){
       ((JSpinner) jc).addChangeListener(this);
     }else
     if(jc instanceof JTextField){
       addName(((JTextField)jc).getDocument(),s);
       ((JTextField) jc).addActionListener(this);
       ((JTextField) jc).getDocument().addDocumentListener(this);
     }else
     if(jc instanceof JTextArea){
       addName(((JTextArea)jc).getDocument(),s);
       ((JTextArea) jc).getDocument().addDocumentListener(this);
     }else
     if(jc instanceof JPanel){
       ((JPanel) jc).addMouseListener(this);
     }else
     if(jc instanceof JLabel){
       ((JLabel) jc).addMouseListener(this);
     }else
     if(jc instanceof JList){
       ((JList) jc).addListSelectionListener(this);
     }else
     if(jc instanceof TableModel){
       ((TableModel) jc).addTableModelListener(this);
     }else
     if(jc instanceof JFrame){
       ((JFrame) jc).addWindowFocusListener(this);
       ((JFrame) jc).addWindowStateListener(this);
       ((JFrame) jc).addWindowListener(this);
       ((JFrame) jc).addComponentListener(this);
       Container p= ((JFrame) jc).getContentPane();
       p.addKeyListener(this);
     }else
     if(jc instanceof JComponent){
       ((JComponent) jc).addMouseListener(this);
     }else{
        System.out.println("Don't know how to listen to "+jc);
     }  
  }

  
  public static boolean isWindowClosing(EventObject e){
    return e!=null&&e instanceof WindowEvent&&
           ((WindowEvent)e).getID()==WindowEvent.WINDOW_CLOSING;}
  public static boolean isWindowResizing(EventObject e){
    return e!=null&&e instanceof ComponentEvent&&
           ((ComponentEvent)e).getID()==ComponentEvent.COMPONENT_RESIZED;}
 
  
  public static boolean isMouseEvent(EventObject e){
    return e!=null&&e instanceof MouseEvent;}
  public static boolean isMousePressed(EventObject e){
    return isMouseEvent(e)&&((MouseEvent)e).getID()==MouseEvent.MOUSE_PRESSED;}
  public static boolean isMouseClicked(EventObject e){
    return isMouseEvent(e)&&((MouseEvent)e).getID()==MouseEvent.MOUSE_CLICKED;}
  public static boolean isMouseReleased(EventObject e){
    return isMouseEvent(e)&&((MouseEvent)e).getID()==MouseEvent.MOUSE_RELEASED;}
  public static int getMouseX(EventObject e){ 
    if(!isMouseEvent(e))return 0;return ((MouseEvent) e).getX(); }
  public static int getMouseY(EventObject e){ 
    if(!isMouseEvent(e))return 0;return ((MouseEvent) e).getY(); }
  public static int getMouseButton(EventObject e){ 
    if(!isMouseEvent(e))return 0;return ((MouseEvent) e).getButton(); }
  public static int getMouseClickCount(EventObject e){ 
    if(!isMouseEvent(e))return 0;return ((MouseEvent) e).getClickCount(); }

  public static boolean isKeyEvent(EventObject e){
    return e!=null&&e instanceof KeyEvent;}
  public static boolean isKeyPressed(EventObject e){
    return isKeyEvent(e)&&((KeyEvent)e).getID()==KeyEvent.KEY_PRESSED;}
  public static boolean isKeyReleased(EventObject e){
    return isKeyEvent(e)&&((KeyEvent)e).getID()==KeyEvent.KEY_RELEASED;}
  public static boolean isKeyTyped(EventObject e){
    return isKeyEvent(e)&&((KeyEvent)e).getID()==KeyEvent.KEY_TYPED;}
  public static boolean isActionKey(EventObject e){
    return isKeyPressed(e)&&((KeyEvent)e).isActionKey();}
  public static char getKeyChar(EventObject e){ 
    if(!isKeyEvent(e))return ' ';return ((KeyEvent) e).getKeyChar(); }
  /** Check whether an event is a  event */
  public static int getKeyCode(EventObject e){ 
    if(!isKeyEvent(e))return 0;return ((KeyEvent) e).getKeyCode(); }
  /** Check whether an event is a  event */
  public static String getKeyText(EventObject e){ 
    String s="";
    if(isKeyEvent(e)&&!isKeyTyped(e))
       s= KeyEvent.getKeyText(((KeyEvent) e).getKeyCode());
    if(isKeyTyped(e))s= Character.toString(((KeyEvent) e).getKeyChar());
    return s; 
  }
 //-----------------------------------------------------

  /** Check whether an event is a  event */
  public static boolean isActionPerformed(EventObject e){
    return (e instanceof ActionEvent)&&
       ((ActionEvent)e).getID()==ActionEvent.ACTION_PERFORMED;}

  public static int getColumn(EventObject e){
    if(e instanceof TableModelEvent)return ((TableModelEvent)e) .getColumn(); 
    return -1;
  } 

  public static int getFirstRow(EventObject e){
    if(e instanceof TableModelEvent)return ((TableModelEvent)e) .getFirstRow(); 
    return -1;
  } 
  public static int getLastRow(EventObject e){
    if(e instanceof TableModelEvent)return ((TableModelEvent)e) .getLastRow(); 
    return -1;
  } 

  //-------------------------------
  //
  //  Timer controls
  //
  private static Map<String,Timer> timers=new HashMap<String,Timer>();

  public void startTimer(int interval,String name){
    name=name.intern();
    if(timers.containsKey(name)){
        System.out.println("already has timer "+name);return;}
    Timer t= new Timer(interval,this);
    t.start();
    timers.put(name,t);
    addName(t,name);
  }
  /** Stop a timer */
  public void stopTimer(String name){
    name=name.intern();
    if(!timers.containsKey(name)){System.out.println("no timer "+name);return;}
    Timer t=timers.get(name);
    t.stop();
    timers.remove(name);
    removeEvents(name);
  }
  /** Sleep a number of milliseconds */
  public void sleep(int ms){
    try{ Thread.sleep(ms);}catch(Exception e){}
  }
 
  /** Create a CheckBoxMenuItem and listen to it */
  public JCheckBoxMenuItem jcheckboxmenuitem(String s){
    JCheckBoxMenuItem jm=new JCheckBoxMenuItem(s);  
    listenTo(jm,s);
    return jm;
  }
  /** Create a RadioButtonMenuItem and listen to it */
  public JRadioButtonMenuItem jradiobuttonmenuitem(String s){
    JRadioButtonMenuItem jm=new JRadioButtonMenuItem(s);  
    listenTo(jm,s);
    return jm;
  }
  /** Create a MenuItem and listen to it */
  public JMenuItem jmenuitem(String s){
    JMenuItem jm=new JMenuItem(s);  
    listenTo(jm,s);
    return jm;
  }
  /** Create a MenuItem and listen to it */
  public JMenuItem jmenuitem(String s,char c){
    JMenuItem jm=new JMenuItem(s,c);  
    listenTo(jm,s);
    return jm;
  }
  /** Create a MenuItem and listen to it */
  public JMenuItem jmenuitem(String s,char c,KeyStroke k){
    JMenuItem jm=new JMenuItem(s,c);  
    jm.setAccelerator(k);
    listenTo(jm,s);
    return jm;
  }
  /** Control-char KeyStroke */
  public static KeyStroke control(char c){
    return KeyStroke.getKeyStroke(c, Toolkit.getDefaultToolkit().
       getMenuShortcutKeyMask(), false);
   }
  /** Control-Shift-char KeyStroke */
  public static KeyStroke controlShift(char c){
    return KeyStroke.getKeyStroke(c,  Toolkit.getDefaultToolkit().
        getMenuShortcutKeyMask()|KeyEvent.SHIFT_MASK, false);
   }
  /** Create a Menu and listen to it */
  public JMenu jmenu(String s,JComponent... j ){
    JMenu jm=new JMenu(s);
    for(JComponent j1:j)jm.add(j1);
    listenTo(jm,s);
    return jm;
  }
  /** Create a Menu and listen to it */
  public JMenu jmenu(String s,char c,JComponent... j ){
    JMenu jm=new JMenu(s);
    jm.setMnemonic(c);
    //jm.setFocusable(true);
    for(JComponent j1:j)jm.add(j1);
    listenTo(jm,s);
    return jm;
  }
  /** Create a MenuBar */
  public JMenuBar jmenubar(Font f,JMenu... j ){
    JMenuBar jm=new JMenuBar();
    for(JMenu j1:j)jm.add(j1);
    setFontRecursively(jm,f);
    return jm;
  }
  /** Create a MenuBar */
  public JMenuBar jmenubar(JMenu... j ){
    JMenuBar jm=new JMenuBar();
    for(JMenu j1:j)jm.add(j1);
    return jm;
  }
  /** Set the font of a component and all its subcomponents */
  public static void setFontRecursively(Component c,Font f){
     if(c==null)return;
     c.setFont(f);
     if(!(c instanceof Container))return;
     Component[] cs=((Container)c).getComponents();
     for(Component c1:cs)setFontRecursively(c1,f);   
     if(!(c instanceof JMenu))return;
     cs=((JMenu)c).getMenuComponents();
     for(Component c1:cs)setFontRecursively(c1,f);   
  }
  //--------------------------------------------------
  // Dialogs
  static EventObject jdialog(JFrame owner,String title,JComponent body,JComponent ...ok){
     JDialog dialog = new JDialog(owner,title);
     dialog.setContentPane(body);
     dialog.pack();
     if(owner!=null)dialog.setLocationRelativeTo(owner);
     else centerLocation(dialog);
     
     JEventQueue events = new JEventQueue();
     if(ok!=null&&ok.length!=0){
        for(int i=0;i<ok.length;i++)
          events.listenTo(ok[i],"ok"+i);
     }
     dialog.addWindowListener(events);
     dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
     
     dialog.setVisible(true);
     EventObject e;
     do{
       e= events.waitEvent();  
     }while((e instanceof WindowEvent)&&(((WindowEvent)e).getID()!=WindowEvent.WINDOW_CLOSING));
     dialog.dispose();
     return e;
  }
  private static void centerLocation(Component c){
    try{
      Dimension d1 = c.getSize();
      Toolkit toolkit = Toolkit.getDefaultToolkit();
      Dimension d2 = toolkit.getScreenSize();
      //System.out.println(d1+" "+d2);
      int x = Math.max(0,(d1.width-d2.width)/2);
      int y = Math.max(0,(d1.height-d2.height)/2);
      c.setLocation(new Point(x,y));
    }catch(Exception e){} 
  } 
}



class DocumentEventObject extends EventObject implements DocumentEvent{
  DocumentEvent de;
  DocumentEventObject(DocumentEvent de1){super(de1);de=de1;}
  public Object getSource(){return de.getDocument();}
  public static final long serialVersionUID = 42L;
  // from DocumentEvent
  public DocumentEvent.ElementChange getChange(Element elem){
    return de.getChange(elem);} 
          //Gets the change information for the given element. 
  public Document getDocument(){return de.getDocument();} 
          //Gets the document that sourced the change event. 
  public int getLength() {return de.getLength();}
          //Returns the length of the change. 
  public int getOffset() {return de.getOffset();}
          //Returns the offset within the document of the start of the change. 
  public DocumentEvent.EventType getType(){return de.getType();} 
          //Gets the type of event. 
}


