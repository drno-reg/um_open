package UM.RU.Scheduler;

public class MainDaemon {

    static private boolean shutdownFlag = false;

    public static int main(String[] args)
    {
        try
        {
            daemonize();
        }
        catch (Throwable e)
        {
            System.err.println("Startup failed. " + e.getMessage());
            return 1;
        }

        registerShutdownHook();

        doProcessing();

        return 0;
    }

    static private void doProcessing()
    {
        while (false == shutdownFlag)
        {
            //Do some processing
        }
    }

    static public void setShutdownFlag() {shutdownFlag = true;}

    private static void registerShutdownHook()
    {
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    public void run() {
                        MainDaemon.setShutdownFlag();
                    }
                }
        );
    }

    static private void daemonize() throws Exception
    {
        System.in.close();
        System.out.close();
    }
}
