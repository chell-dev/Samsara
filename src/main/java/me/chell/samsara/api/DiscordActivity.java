package me.chell.samsara.api;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import me.chell.samsara.Samsara;
import me.chell.samsara.impl.module.misc.DiscordRPC;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Instant;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DiscordActivity implements Loadable {
    private static DiscordThread thread;
    private static File discordLibrary;

    // https://github.com/JnCrMx/discord-game-sdk4j/blob/master/examples/ActivityExample.java
    @Override
    public void load() {
        try {
            discordLibrary = downloadDiscordLibary();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(discordLibrary == null) {
            Samsara.LOGGER.error("Couldn't download Discord Game SDK.");
        }
    }

    @Override
    public void unload() {
        stop();
    }

    public static void start(DiscordRPC.DiscordValue... activity) throws RuntimeException {
        if(discordLibrary == null) throw new RuntimeException();
        if(thread != null) return;
        thread = new DiscordThread(discordLibrary, activity);
        thread.start();
    }

    public static void stop() {
        if(thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    // https://github.com/JnCrMx/discord-game-sdk4j/blob/master/examples/DownloadNativeLibrary.java
    private File downloadDiscordLibary() throws IOException {
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if(osName.contains("windows")) {
            suffix = ".dll";
        }
        else if(osName.contains("linux")) {
            suffix = ".so";
        }
        else if(osName.contains("mac os")) {
            suffix = ".dylib";
        }
        else {
            return null;
        }

        /*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */
        if(arch.equals("amd64"))
            arch = "x86_64";

        // Check if we already downloaded the library
        File f = new File(Samsara.FOLDER, name+suffix);
        if(f.exists()) return f;

        // Path of Discord's library inside the ZIP
        String zipPath = "lib/"+arch+"/"+name+suffix;

        // Open the URL as a ZipInputStream
        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        ZipInputStream zin = new ZipInputStream(downloadUrl.openStream());

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while((entry = zin.getNextEntry())!=null) {
            if(entry.getName().equals(zipPath)) {

                File file = new File(Samsara.FOLDER, name+suffix);

                // Copy the file in the ZIP to our file
                Files.copy(zin, file.toPath());

                // We are done, so close the input stream
                zin.close();

                // Return our file
                return file;
            }
            // next entry
            zin.closeEntry();
        }
        zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }

    public static class DiscordThread extends Thread {
        private final File discordLibrary;
        public DiscordRPC.DiscordValue details, state, image;

        public DiscordThread(File discordLibrary, DiscordRPC.DiscordValue... activity) {
            this.discordLibrary = discordLibrary;
            details = activity[0];
            state = activity[1];
            image = activity[2];
        }

        @Override
        public void run() {
            Core.init(discordLibrary);

            try(CreateParams params = new CreateParams()) {
                params.setClientID(918115168599281694L);
                params.setFlags(CreateParams.getDefaultFlags());

                try(Core core = new Core(params)) {
                    try(Activity activity = new Activity()) {
                        activity.setDetails(details.getVal());
                        activity.setState(state.getVal());

                        activity.timestamps().setStart(Instant.now());

                        activity.assets().setLargeImage("largeImage");
                        activity.assets().setLargeText(image.getVal());
                        //activity.assets().setSmallImage("cat");
                        //activity.assets().setSmallText("Small text");

                        while(!isInterrupted()) {
                            activity.setDetails(details.getVal());
                            activity.setState(state.getVal());
                            activity.assets().setLargeText(image.getVal());
                            core.activityManager().updateActivity(activity);

                            core.runCallbacks();
                            try {
                                // Sleep a bit to save CPU
                                sleep(16);
                            } catch(InterruptedException e) {
                                core.close();
                            }
                        }
                        core.close();
                    }
                }
            }
        }
    }
}
