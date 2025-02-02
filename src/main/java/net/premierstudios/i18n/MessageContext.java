package net.premierstudios.i18n;

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Locale;

public abstract class MessageContext
{
	public abstract TagResolver[] translateArguments(Locale locale);
}
