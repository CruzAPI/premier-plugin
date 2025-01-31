package net.premierstudios.functional;

@FunctionalInterface
public interface ContextBuilder<C>
{
	C build(C context);
}
