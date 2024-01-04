package io.github.wickeddroid.plugin.message.announcements;

import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.title.Title;

import java.util.List;

public record GameAnnouncement(Title title, String time, Sound sound, List<String> scenarios) {}
