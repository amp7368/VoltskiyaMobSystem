-- apply changes
create table dstored_mob (
  id                            bigint auto_increment not null,
  mob_type                      integer not null,
  location_world                smallint(3),
  location_x                    bigint(28),
  location_y                    bigint(28),
  location_z                    bigint(28),
  location_x_decimal            double(16,16),
  location_y_decimal            double(16,16),
  location_z_decimal            double(16,16),
  location_yaw                  float,
  location_pitch                float,
  spawn_delay                   bigint not null,
  constraint pk_dstored_mob primary key (id)
);

create table stored_world (
  id                            smallint auto_increment not null,
  bukkit                        varchar(40) not null,
  spawn_success_count           bigint not null,
  spawn_fail_count              bigint not null,
  constraint pk_stored_world primary key (id)
);

