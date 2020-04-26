package com.example.repository

import com.example.domain.{Catalog, SearchArticle}
import java.time.LocalDateTime

import doobie.util.{Read, Write}
import doobie.util.update.Update
import doobie.postgres._
import doobie.postgres.implicits._
import doobie.util.ExecutionContexts
import cats.implicits._
import com.example.domain.{Article, Catalog, SearchArticle}
import doobie._
import doobie.implicits._
import doobie.implicits.javatime._

object ContainerRepository {

  implicit val articleWrite: Write[Article] =
    Write[(LocalDateTime, LocalDateTime, String, String, Array[String], String, Array[String])]
      .contramap[Article](
        x =>
          (
            x.create_timestamp.toLocalDateTime,
            x.timestamp.toLocalDateTime,
            x.language,
            x.wiki,
            x.category,
            x.title,
            x.auxiliary_text
          )
      )

  implicit val searchArticleRead: Read[SearchArticle] =
    Read[(Long, Long, String, Array[String])].map { case (a, b, c, d) => new SearchArticle(a, b, c, d) }

  implicit val catalogRead: Read[Catalog] =
    Read[(Array[String], Int)].map { case (a, b) => new Catalog(a, b) }

  def dropCatalog =
    sql"""DROP TABLE IF EXISTS public."Catalog";""".update.run

  def dropArticle =
    sql"""DROP TABLE IF EXISTS public."Articles";""".update.run

  def createArticle =
    sql"""
    CREATE TABLE public."Articles"
    (
        create_timestamp timestamp with time zone NOT NULL,
        "timestamp" timestamp with time zone NOT NULL,
        language character varying COLLATE pg_catalog."default" NOT NULL,
        wiki character varying COLLATE pg_catalog."default" NOT NULL,
        category character varying[] COLLATE pg_catalog."default"  NOT NULL,
        title character varying COLLATE pg_catalog."default" NOT NULL,
        auxiliary_text character varying[] COLLATE pg_catalog."default" NOT NULL
    );
    """.update.run

  def createCatalog =
    sql"""
     CREATE TABLE public."Catalog"
      (
          id serial primary key,
          category character varying[] COLLATE pg_catalog."default" NOT NULL,
          amount integer NOT NULL
      );
     """.update.run

  def createReference =
    sql"""Alter table public."Articles"
          Add column category_id integer references public."Catalog";""".update.run

  def insertManyArticle(data: List[Article]) = {
    val sql =
      """insert into public."Articles" (create_timestamp, "timestamp", language,
          wiki, category, title, auxiliary_text)
          values (?, ?, ?, ?, ?, ?, ?)"""
    Update[Article](sql)
      .updateMany(data)
  }

  def createIndTitle =
    sql"""CREATE INDEX "Ind_title"
             ON public."Articles" USING btree
             (title ASC NULLS LAST)
             TABLESPACE pg_default;""".update.run

  def insertCatalog =
    sql"""
        insert into public."Catalog" (category, amount)
        select category, count(1) from public."Articles" group by category;
        """.update.run

  def updArticle =
    sql"""Update public."Articles" set category_id = c.id
         from public."Catalog" as c where c.category = "Articles".category;
         Alter table public."Articles"
         drop column category,
         Alter column category_id set not null;
         """.update.run

  def find(title: String) =
    sql"""SELECT
          cast(extract(epoch from create_timestamp) as BIGINT) as create_timestamp,
          cast(extract(epoch from "timestamp") as BIGINT) as "timestamp",
          title,
          auxiliary_text
        FROM public."Articles" where title = $title;"""
      .query[SearchArticle]

  def getCatalog =
    sql"""
         SELECT category, amount
	        FROM public."Catalog";
         """
      .query[Catalog]
}
