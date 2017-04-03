create table cache(
  utm VARCHAR(8) not null,
  "date" date not null,
  "exists" boolean not null,
  PRIMARY KEY (utm, "date")
);

create table tasks(
  utm VARCHAR(8) not null,
  "date" date not null,
  cloudiness double not null,
  PRIMARY KEY (utm, "date")
);
