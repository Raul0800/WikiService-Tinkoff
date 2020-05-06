drop table IF EXISTS public."Articles";

create TABLE public."Articles"
    (
        create_timestamp timestamp with time zone NOT NULL,
        "timestamp" timestamp with time zone NOT NULL,
        language character varying COLLATE pg_catalog."default" NOT NULL,
        wiki character varying COLLATE pg_catalog."default" NOT NULL,
        category character varying[] COLLATE pg_catalog."default"  NOT NULL,
        title character varying COLLATE pg_catalog."default" NOT NULL,
        auxiliary_text character varying[] COLLATE pg_catalog."default" NOT NULL
    );

CREATE INDEX "Ind_title"
             ON public."Articles" USING btree
             (title ASC NULLS LAST)
             TABLESPACE pg_default;

create view "Statistic"
as
    select category,
    count(category) as count
from public."Articles"
    group by category;