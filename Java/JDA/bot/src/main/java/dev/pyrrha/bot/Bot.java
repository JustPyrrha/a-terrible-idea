package dev.pyrrha.bot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.login.LoginException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

public class Bot extends ListenerAdapter {

    public static void main(String... args) throws LoginException {
        if (args.length != 2) return;
        JDABuilder.createLight(args[0])
                .addEventListeners(new Bot(Long.parseLong(args[1])))
                .build();
    }

    private final Long guildId;

    public Bot(Long guildId) {
        this.guildId = guildId;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        @Nullable Guild guild = event.getJDA().getGuildById(this.guildId);
        if (guild != null) {
            guild.upsertCommand(new CommandData("ping", "Pong!")).queue();
            guild.upsertCommand(new CommandData("info", "General user info.")).queue();
        }
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.isFromGuild() && Objects.requireNonNull(event.getGuild()).getIdLong() == this.guildId) {
            switch (event.getName()) {
                case "ping" -> event.reply("Pong! `%sms`".formatted(event.getJDA().getGatewayPing())).queue();
                case "info" -> event.replyEmbeds(
                                new EmbedBuilder()
                                        .setAuthor(event.getUser().getName(), null, event.getUser().getAvatarUrl())
                                        .addField("Register Date", event.getUser().getTimeCreated().format(DateTimeFormatter.ISO_DATE_TIME), true)
                                        .addBlankField(true)
                                        .addField("Join Date", event.getMember().getTimeJoined().format(DateTimeFormatter.ISO_DATE_TIME), true)
                                        .addField("User Flags", event.getUser().getFlags().stream().map(User.UserFlag::getName).collect(Collectors.joining("\n")), true)
                                        .addBlankField(true)
                                        .addField("Roles (%s)".formatted(event.getMember().getRoles().size()), event.getMember().getRoles().stream().map(Role::getAsMention).collect(Collectors.joining("\n")), true)
                                        .setTimestamp(OffsetDateTime.now())
                                        .build())
                        .queue();
            }
        }
    }
}
