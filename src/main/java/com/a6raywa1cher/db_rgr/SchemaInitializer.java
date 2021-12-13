package com.a6raywa1cher.db_rgr;

import com.a6raywa1cher.db_rgr.dblib.EntityManager;

import java.sql.SQLException;

public class SchemaInitializer {
	public static void createTables(EntityManager em) throws SQLException {
		em.execute("""
			CREATE TABLE IF NOT EXISTS public.department (
				department_title varchar(255) PRIMARY KEY,
				tel_number varchar(50),
				address varchar(255)
			);
			CREATE TABLE IF NOT EXISTS public.machinery_type (
				machinery_title varchar(255) PRIMARY KEY,
				type varchar(255),
				life_time_years int CHECK ( life_time_years > 0 )
			);
			CREATE TABLE IF NOT EXISTS public.employee_type (
				employee_rank varchar(255) NOT NULL,
				position varchar(255) NOT NULL,
				retirement_after_years int CHECK ( retirement_after_years > 0 ),
				PRIMARY KEY (employee_rank, position)
			);
			CREATE TABLE IF NOT EXISTS public.employee (
				full_name varchar(255),
				department_title varchar(255) REFERENCES public.department
					ON UPDATE CASCADE ON DELETE CASCADE,
				position varchar(255) NOT NULL,
				employee_rank varchar(255) NOT NULL,
				birth_date date CHECK ( birth_date < now()::date ),
				employment_date date CHECK ( employment_date <= now()::date ),
				last_promotion_date date CHECK ( last_promotion_date <= now()::date ),
				PRIMARY KEY (full_name, department_title),
				FOREIGN KEY (position, employee_rank) REFERENCES public.employee_type (position, employee_rank)
					MATCH FULL ON UPDATE CASCADE ON DELETE RESTRICT
			);
			CREATE TABLE IF NOT EXISTS public.machinery_requirement (
				department_title varchar(255) REFERENCES public.department
					ON UPDATE CASCADE ON DELETE CASCADE,
				machinery_title varchar(255) REFERENCES public.machinery_type
					ON UPDATE CASCADE ON DELETE CASCADE,
				count int CHECK (count > 0) NOT NULL,
				PRIMARY KEY (department_title, machinery_title)
			);
			CREATE TABLE IF NOT EXISTS public.employee_requirement (
				department_title varchar(255) REFERENCES public.department
					ON UPDATE CASCADE ON DELETE CASCADE,
				employee_rank varchar(255),
				position varchar(255),
				count int CHECK (count > 0) NOT NULL,
				PRIMARY KEY (department_title, employee_rank, position),
				FOREIGN KEY (employee_rank, position) REFERENCES public.employee_type (employee_rank, position)
					MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE
			);
			CREATE TABLE IF NOT EXISTS public.machinery (
				department_title varchar(255) REFERENCES public.department
					ON UPDATE CASCADE ON DELETE CASCADE,
				id int CHECK (id > 0),
				machinery_title varchar(255) REFERENCES public.machinery_type
					ON UPDATE CASCADE ON DELETE RESTRICT,
				holder_name varchar(255),
				date_of_purchase date CHECK ( date_of_purchase <= now()::date ),
				PRIMARY KEY (department_title, machinery_title, id),
				FOREIGN KEY (holder_name, department_title) REFERENCES public.employee (full_name, department_title)
					MATCH SIMPLE ON UPDATE CASCADE ON DELETE RESTRICT
			);
			""");
	}

	public static void dropDatabase(EntityManager em) throws SQLException {
		em.execute("""
			drop table if exists machinery_requirement cascade;
			drop table if exists employee_requirement cascade;
			drop table if exists machinery cascade;
			drop table if exists machinery_type cascade;
			drop table if exists employee cascade;
			drop table if exists department cascade;
			drop table if exists employee_type cascade;
			""");
	}
}
