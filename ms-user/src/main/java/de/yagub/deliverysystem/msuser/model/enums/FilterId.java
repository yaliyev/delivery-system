package de.yagub.deliverysystem.msuser.model.enums;

public enum FilterId {

  DUPLICATE_USERNAME_FILTER(3),
  PASSWORD_STRENGTH_FILTER (2),
  USERNAME_VALIDATION_FILTER(1);

  private final int id;

  private FilterId(int id){
    this.id = id;
  }

  public int getId() {
    return id;
  }

}
