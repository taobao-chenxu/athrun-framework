/**
 * 
 */


/**
 * @author taichan
 * 
 */
public abstract class OneParameterRunnable implements Runnable {

	/**
	 * 
	 */
	public OneParameterRunnable() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Object o;

	/**
	 * 
	 */
	public OneParameterRunnable(Object o) {
		super();
		this.o = o;
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public abstract void run();

	public Object getParameter() {
		return o;
	}

}
