import play.jobs.Job;
import play.jobs.OnApplicationStart;

@OnApplicationStart
public class Configure extends Job
{
    @Override
    public void doJob() throws Exception
    {
        //FrontContainer.getInstance().use(IApplicationSettings.class, new FrontSettings());
    }
}
