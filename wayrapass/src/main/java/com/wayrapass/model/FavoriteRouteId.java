package com.wayrapass.model;
import lombok.*; import java.io.Serializable;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode public class FavoriteRouteId implements Serializable { private Long student; private Long route; }
