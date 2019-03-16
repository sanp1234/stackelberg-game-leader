import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import comp34120.ex2.Record;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

final class OurLeader
	extends PlayerImpl
{

	private List<Record> data;

	// R(u_l) = a + b(u_l)
	private Model reaction;

	private OurLeader()
		throws RemoteException, NotBoundException
	{
		super(PlayerType.LEADER, "Our Leader");
	}

	@Override
	public void goodbye()
		throws RemoteException
	{
		ExitTask.exit(500);
	}

	@Override
	public void startSimulation(int p_steps)
		throws RemoteException
	{
		data = getData();
		for(Record record : data) {
			System.out.println(String.format("Record %d :: Leader: %f, Follower: %f", record.m_date, record.m_leaderPrice, record.m_followerPrice));
		}
		reaction = new Model();
		reaction.train(data);
	}

	/**
	 * To inform this instance to proceed to a new simulation day
	 * @param p_date The date of the new day
	 * @throws RemoteException
	 */
	@Override
	public void proceedNewDay(int p_date)
		throws RemoteException
	{
		m_platformStub.publishPrice(m_type, genPrice(1.8f, 0.05f));
	}

	/**
	 * Generate a random price based Gaussian distribution. The mean is p_mean,
	 * and the diversity is p_diversity
	 * @param p_mean The mean of the Gaussian distribution
	 * @param p_diversity The diversity of the Gaussian distribution
	 * @return The generated price
	 */
	private float genPrice(final float p_mean, final float p_diversity)
	{
		return 1.0f;
	}

	private List<Record> getData() throws RemoteException{
		List<Record> records = new ArrayList<>();
		for (int date = 1; date <= 100; date++) {
			records.add(m_platformStub.query(PlayerType.LEADER, date));
		}
		return records;
	}

	public static void main(final String[] p_args)
		throws RemoteException, NotBoundException
	{
		new OurLeader();
	}

	/**
	 * The task used to automatically exit the leader process
	 * @author Xin
	 */
	private static class ExitTask
		extends TimerTask
	{
		static void exit(final long p_delay)
		{
			(new Timer()).schedule(new ExitTask(), p_delay);
		}

		@Override
		public void run()
		{
			System.exit(0);
		}
	}

	private static class Model {
		private double a;
		private double b;

		public void train(List<Record> records) {

		}

		public double predict(double u) {
			return a + b * u;
		}
	}
}