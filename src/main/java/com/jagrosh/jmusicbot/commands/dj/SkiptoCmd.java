/*
 * Copyright 2016 John Grosh <john.a.grosh@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jagrosh.jmusicbot.commands.dj;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jmusicbot.Bot;
import com.jagrosh.jmusicbot.audio.AudioHandler;
import com.jagrosh.jmusicbot.commands.DJCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;

/**
 *
 * @author John Grosh <john.a.grosh@gmail.com>
 */
public class SkiptoCmd extends DJCommand 
{
    public SkiptoCmd(Bot bot)
    {
        super(bot);
        this.name = "skipto";
        this.help = "skips to the specified song";
        this.arguments = "<position>";
        this.aliases = bot.getConfig().getAliases(this.name);
        this.bePlaying = true;
        this.options = Collections.singletonList(new OptionData(OptionType.INTEGER, "position", "Position to skip to", true));
    }

    @Override
    public void doCommand(SlashCommandEvent event)
    {
        int index = (int) event.getOption("Position").getAsLong();
        AudioHandler handler = (AudioHandler)event.getGuild().getAudioManager().getSendingHandler();
        if(index<1 || index>handler.getQueue().size())
        {
            event.reply(getClient().getError()+" Position must be a valid integer between 1 and "+handler.getQueue().size()+"!")
                .setEphemeral(true)
                .queue();
            return;
        }
        handler.getQueue().skip(index-1);
        event.reply(getClient().getSuccess()+" Skipped to **"+handler.getQueue().get(0).getTrack().getInfo().title+"**")
            .allowedMentions(Collections.emptyList())
            .queue();
        handler.getPlayer().stopTrack();
    }
}
