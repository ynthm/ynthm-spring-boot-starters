package com.ynthm.common.context;

/**
 * @author Ethan Wang
 * @version 1.0
 */
final class ThreadLocalUserContextHolderStrategy implements UserContextHolderStrategy {
  private static final ThreadLocal<UserContext> CONTEXT_HOLDER = new ThreadLocal();

  public ThreadLocalUserContextHolderStrategy() {}

  @Override
  public void clearContext() {
    CONTEXT_HOLDER.remove();
  }

  @Override
  public UserContext getContext() {
    UserContext ctx = CONTEXT_HOLDER.get();
    if (ctx == null) {
      CONTEXT_HOLDER.set(ctx);
    }

    return ctx;
  }

  @Override
  public void setContext(UserContext context) {
    //    Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
    CONTEXT_HOLDER.set(context);
  }
}
